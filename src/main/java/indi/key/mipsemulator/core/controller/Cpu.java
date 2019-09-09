package indi.key.mipsemulator.core.controller;

import java.util.HashSet;
import java.util.Set;

import indi.key.mipsemulator.core.action.Action;
import indi.key.mipsemulator.core.action.BranchAction;
import indi.key.mipsemulator.core.action.ITypeAction;
import indi.key.mipsemulator.core.action.JumpAction;
import indi.key.mipsemulator.core.action.MemoryAction;
import indi.key.mipsemulator.core.action.RTypeAction;
import indi.key.mipsemulator.core.model.CpuStatistics;
import indi.key.mipsemulator.core.model.Instruction;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.exception.NotImplementedException;
import indi.key.mipsemulator.model.info.BitArray;
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

    boolean delaySlotEnable = false; // TODO

    public Cpu(Machine machine) {
        this.machine = machine;
        this.pc = machine.getRegister(RegisterType.PC);
        reset();
    }

    @Override
    public void reset() {
        looping = false;
        instructionCache = new Runnable[MemoryType.RAM.getCapacity() / 4 + 1];
    }

    public void addCpuListener(CpuCallback callback) {
        callbackSet.add(callback);
    }

    private void notifyListeners() {
        for (CpuCallback callback : callbackSet) {
            callback.onCpuNext(pc);
        }
    }

    public boolean isLooping() {
        return looping;
    }

    private void single() {
        try {
            getStatementRunnable(this).run();
        } catch (Exception e) {
            LogUtils.m(e.getMessage());
        }
    }

    public void singleStep() {
        single();
        notifyListeners();
    }

    public void singleStepWithoutJal() {
        int pcValue = pc.get();
        single();
        pc.set(pcValue + 4);
        notifyListeners();
    }

    public void loop() {
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
                    int index = pcCurrent / 4;
                    if (index >= instructionCache.length || instructionCache[index] == null) {
                        instructionCache[index] = getStatementRunnable(Cpu.this);
                    }

                    instructionCount++;
                    instructionCache[index].run();
                } catch (Exception exception) {
                    errorCount++;
                    resentException = exception;
                    exceptionAddress = pcCurrent;
                }
                //machine.ticks();
            }
        }).start();
    }

    public CpuStatistics exitLoop() {
        looping = false;
        //counter.endTicking();
        if (resentException != null) {
            resentException.printStackTrace();
        }
        notifyListeners();
        return new CpuStatistics(System.currentTimeMillis() - startTime, instructionCount,
                errorCount, resentException, exceptionAddress);
    }

    private static Runnable getStatementRunnable(Cpu cpu) {
        Register pc = cpu.pc;
        int index = pc.get();
        Statement statement = cpu.machine.loadStatement(index);
        Instruction instruction = statement.getInstruction();
        Action action = instruction.getAction();
        boolean linkNext = instruction.isLinkNext();
        Register rs = cpu.machine.getRegister(statement.getRs());
        Register rt = cpu.machine.getRegister(statement.getRt());
        Register rd = cpu.machine.getRegister(statement.getRd());
        Register ra = cpu.machine.getRegister(RegisterType.RA);
        BitArray immediate = statement.getImmediate();
        int shamt = statement.getShamt().value();

        if (action instanceof RTypeAction) {
            RTypeAction rTypeAction = (RTypeAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                rTypeAction.execute(cpu.machine, rs, rt, rd, shamt);
                afterExcution(cpu);
            };
        } else if (action instanceof ITypeAction) {
            ITypeAction iTypeAction = (ITypeAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                iTypeAction.execute(cpu.machine, rs, rt, immediate);
                afterExcution(cpu);
            };
        } else if (action instanceof BranchAction) {
            BranchAction branchAction = (BranchAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                boolean check = branchAction.check(cpu.machine, rs, rt);
                if (check) {
                    pc.set(pc.get() + statement.getOffset() * 4);
                }
                afterExcution(cpu);
            };
        } else if (action instanceof JumpAction) {
            JumpAction jumpAction = (JumpAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                pc.set(jumpAction.getNext(cpu.machine, statement));
                afterExcution(cpu);
            };
        } else if (action instanceof MemoryAction) {
            MemoryAction memoryAction = (MemoryAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                memoryAction.execute(cpu.machine, rs.getUnsigned() + immediate.integerValue(), rt);
                afterExcution(cpu);
            };
        } else {
            return () -> {
                throw new NotImplementedException("Action " + instruction.name() + " haven't been implemented.");
            };
        }
    }

    private void track(int index, Statement statement) {
        if (!looping) {
            LogUtils.i(index + ": " + statement.toString());
        }
    }

    private static void beforeExcution(Register pc, Register ra, boolean linkNext) {
        // Move pc to loop;
        pc.set(pc.get() + 4);
        // Link the address to $ra
        if (linkNext) {
            ra.set(pc.get());
        }
    }

    private static void afterExcution(Cpu cpu) {

    }
}
