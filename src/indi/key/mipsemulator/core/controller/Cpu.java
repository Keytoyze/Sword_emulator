package indi.key.mipsemulator.core.controller;

import java.util.HashSet;
import java.util.Set;

import indi.key.mipsemulator.core.action.Action;
import indi.key.mipsemulator.core.action.ConditionalAction;
import indi.key.mipsemulator.core.action.ITypeAction;
import indi.key.mipsemulator.core.action.JumpAction;
import indi.key.mipsemulator.core.action.MemoryAction;
import indi.key.mipsemulator.core.action.RTypeAction;
import indi.key.mipsemulator.core.model.CpuStatistics;
import indi.key.mipsemulator.core.model.Instruction;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.exception.NotImplementedException;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import indi.key.mipsemulator.util.LogUtils;

public class Cpu implements Resetable {

    private Set<TickCallback> callbackSet = new HashSet<>();
    private Register pc;
    private Machine machine;

    // For looping
    private boolean looping;
    private Exception resentException;
    private long startTime;
    private int instructionCount;
    private int errorCount;

    // Use cache to speed loop
    private Runnable[] instructionCache;

    public Cpu(Machine machine) {
        this.machine = machine;
        this.pc = machine.getRegister(RegisterType.PC);
        reset();
    }

    @Override
    public void reset() {
        instructionCache = new Runnable[MemoryType.RAM.getLength() / 4 + 1];
    }

    public void addCpuListener(TickCallback tickCallback) {
        callbackSet.add(tickCallback);
    }

    public boolean isLooping() {
        return looping;
    }

    public void singleStep() {
        getStatementRunnable(this).run();
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
                try {
                    int index = pc.get() / 4;
                    if (instructionCache[index] == null) {
                        instructionCache[index] = getStatementRunnable(Cpu.this);
                    }
                    instructionCount++;
                    instructionCache[index].run();
                } catch (Exception exception) {
                    errorCount++;
                    resentException = exception;
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
        return new CpuStatistics(System.currentTimeMillis() - startTime, instructionCount, errorCount);
    }

    private static Runnable getStatementRunnable(Cpu cpu) {
        Register pc = cpu.pc;
        int index = pc.get();
        Statement statement = Statement.of(cpu.machine.loadInt(index));
        Instruction instruction = statement.getInstruction();
        Action action = instruction.getAction();
        boolean linkNext = instruction.isLinkNext();
        Register rs = cpu.machine.getRegister(statement.getRs());
        Register rt = cpu.machine.getRegister(statement.getRt());
        Register rd = cpu.machine.getRegister(statement.getRd());
        Register ra = cpu.machine.getRegister(RegisterType.RA);
        BitArray immediate = statement.getImmediate();
        int shamt = statement.getShamt();

        if (action instanceof RTypeAction) {
            RTypeAction rTypeAction = (RTypeAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                rTypeAction.execute(cpu.machine, rs, rt, rd, shamt);
            };
        } else if (action instanceof ITypeAction) {
            ITypeAction iTypeAction = (ITypeAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                iTypeAction.execute(cpu.machine, rs, rt, immediate);
                afterExcution(cpu);
            };
        } else if (action instanceof ConditionalAction) {
            ConditionalAction conditionalAction = (ConditionalAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                boolean check = conditionalAction.check(cpu.machine, rs, rt);
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
        for (TickCallback callback : cpu.callbackSet) {
            callback.onTick();
        }
    }
}
