package indi.key.mipsemulator.core.controller;

import java.util.HashSet;
import java.util.Set;

import indi.key.mipsemulator.core.action.Action;
import indi.key.mipsemulator.core.model.CpuStatistics;
import indi.key.mipsemulator.core.model.DelaySlotType;
import indi.key.mipsemulator.core.model.Instruction;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.interfaces.CpuCallback;
import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import indi.key.mipsemulator.util.LogUtils;

public class Cpu implements Resetable {

    private Set<CpuCallback> callbackSet = new HashSet<>();
    private Register pc;
    private Machine machine;

    // For looping
    private volatile boolean looping;
    private Exception resentException;
    private long exceptionAddress;
    private long startTime;
    private long instructionCount;
    private long errorCount;

    // Use cache to speed loop
    private Runnable[] instructionCache;

    // jump to this address after delay slot has ever been executed. (valid when delay slot is enable)
    private long nextAddress = -1;
    private boolean isInDelaySlot = false;

    boolean delaySlotEnable = false;

    Cpu(Machine machine) {
        this.machine = machine;
        this.pc = machine.getRegister(RegisterType.PC);
        reset();
    }

    @Override
    public void reset() {
        looping = false;
        instructionCache = new Runnable[MemoryType.RAM.getCapacity() / 4 + 1];
    }

    void addCpuListener(CpuCallback callback) {
        callbackSet.add(callback);
    }

    private void notifyListeners() {
        for (CpuCallback callback : callbackSet) {
            callback.onCpuNext(pc);
        }
    }

    boolean isLooping() {
        return looping;
    }

    private void single() {
        try {
            next();
        } catch (Exception e) {
            LogUtils.m(e.getMessage());
        }
    }

    private void next() {
        int index = pc.get() / 4;
        if (index >= instructionCache.length || instructionCache[index] == null) {
            instructionCache[index] = getStatementRunnable();
        }
        instructionCache[index].run();
        if (delaySlotEnable) {
            if (isInDelaySlot) {
                pc.setUnsigned(nextAddress);
                isInDelaySlot = false;
                nextAddress = -1;
            } else if (nextAddress >= 0) {
                isInDelaySlot = true;
            }
        }
    }

    void singleStep() {
        single();
        notifyListeners();
    }

    void singleStepWithoutJal() {
        int pcValue = pc.get();
        single();
        pc.set(pcValue + 4);
        nextAddress = -1;
        notifyListeners();
    }

    void loop() {
        looping = true;
        instructionCount = 0;
        errorCount = 0;
        resentException = null;
        startTime = System.currentTimeMillis();
        //counter.beginTicking();
        new Thread(() -> {
            while (looping) {
                int pcCurrent = pc.get();
                try {
                    next();
                    instructionCount++;
                } catch (Exception exception) {
                    errorCount++;
                    resentException = exception;
                    exceptionAddress = pcCurrent;
                }
                //machine.ticks();
            }
        }).start();
    }

    CpuStatistics exitLoop() {
        looping = false;
        //counter.endTicking();
        if (resentException != null) {
            resentException.printStackTrace();
        }
        notifyListeners();
        return new CpuStatistics(System.currentTimeMillis() - startTime, instructionCount,
                errorCount, resentException, exceptionAddress);
    }

    private Runnable getStatementRunnable() {
        int index = pc.get();
        Statement statement = machine.loadStatement(index);
        Instruction instruction = statement.instruction;
        Action action = instruction.getAction();
        boolean linkNext = instruction.isLinkNext();
        Register ra = machine.getRegister(RegisterType.RA);

        return () -> {
            // Move pc to loop;
            pc.set(pc.get() + 4);
            // Link the address to $ra
            if (linkNext) {
                if (instruction == Instruction.JALR) {
                    statement.rdReg.set(pc.get());
                } else {
                    ra.set(pc.get());
                }
            }
            action.execute(machine, Cpu.this, statement);
        };
    }

    public void changeAddress(int delaySlotType, long address) {
        if (!delaySlotEnable || delaySlotType != DelaySlotType.ALWAYS) {
            pc.setUnsigned(address);
        } else {
            nextAddress = address;
            isInDelaySlot = false;
        }
    }

    public int getPc() {
        return pc.get();
    }
}
