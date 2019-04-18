package indi.key.mipsemulator.core.controller;

import java.io.File;

import indi.key.mipsemulator.core.action.Action;
import indi.key.mipsemulator.core.action.MemoryAction;
import indi.key.mipsemulator.core.action.ConditionalAction;
import indi.key.mipsemulator.core.action.ITypeAction;
import indi.key.mipsemulator.core.model.CpuStatistics;
import indi.key.mipsemulator.core.model.Instruction;
import indi.key.mipsemulator.core.action.JumpAction;
import indi.key.mipsemulator.core.action.RTypeAction;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.interfaces.RegisterListener;
import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import indi.key.mipsemulator.storage.AddressRedirector;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.model.exception.NotImplementedException;
import indi.key.mipsemulator.model.bean.BitArray;
import indi.key.mipsemulator.util.LogUtils;
import indi.key.mipsemulator.util.TimingRenderer;
import javafx.application.Platform;

public class Cpu implements Resetable, TickCallback {

    private Register[] registers;
    private AddressRedirector addressRedirector;
    private RegisterListener registerListener;

    // For looping
    private boolean looping;

    private long startTime;
    private int instructionCount;
    private int errorCount;

    // Use cache to speed loop
    private Runnable[] instructionCache;

    public Cpu(File initFile) {
        this.addressRedirector = new AddressRedirector(initFile);
        registers = new Register[RegisterType.values().length];
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register(RegisterType.of(i), this);
        }
        reset();
    }

    @Override
    public void reset() {
        for (Register register : registers) {
            register.reset();
        }
        addressRedirector.reset();
        instructionCache = new Runnable[MemoryType.RAM.getLength() / 4 + 1];
    }

    public Register getRegister(RegisterType registerType) {
        return registers[registerType.ordinal()];
    }

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public void notifyRegisterChange(Register register) {
        if (!looping) {
            // notify asynchronously
            Platform.runLater(() -> {
                if (registerListener != null) {
                    registerListener.onRegisterChange(register);
                }
            });
        }
        // The system will notify registers synchronously in the onTick()
    }


    public Register getHI() {
        return registers[RegisterType.HI.ordinal()];
    }

    public Register getLO() {
        return registers[RegisterType.LO.ordinal()];
    }

    public byte[] loadMemory(long address, int bytesNum) {
        return addressRedirector.load(address, bytesNum);
    }

    public int loadInt(long address) {
        return addressRedirector.loadInt(address);
    }

    public void saveMemory(long address, byte[] data) {
        addressRedirector.save(address, data);
    }

    public void saveInt(long address, int data) {
        addressRedirector.saveInt(address, data);
    }

    public boolean isLooping() {
        return looping;
    }

    public void singleStep() {
        if (looping) {
            return;
        }
        getStatementRunnable(this).run();
    }

    public void loop() {
        looping = true;
        instructionCount = 0;
        errorCount = 0;
        startTime = System.currentTimeMillis();
        new Thread(() -> {
            while (looping) {
                Register pc = getRegister(RegisterType.PC);
                int index = pc.get() / 4;
                if (instructionCache[index] == null) {
                    instructionCache[index] = getStatementRunnable(Cpu.this);
                }
                instructionCount++;
                try {
                    instructionCache[index].run();
                } catch (Exception exception) {
                    errorCount++;
                }
            }
        }).start();
        TimingRenderer.register(this);
    }

    public CpuStatistics exitLoop() {
        looping = false;
        TimingRenderer.unRegister(this);
        return new CpuStatistics(System.currentTimeMillis() - startTime, instructionCount, errorCount);
    }

    private static Runnable getStatementRunnable(Cpu cpu) {
        Register pc = cpu.getRegister(RegisterType.PC);
        int index = pc.get();
        Statement statement = Statement.of(cpu.addressRedirector.loadInt(index));
        Instruction instruction = statement.getInstruction();
        Action action = instruction.getAction();
        boolean linkNext = instruction.isLinkNext();
        Register rs = cpu.getRegister(statement.getRs());
        Register rt = cpu.getRegister(statement.getRt());
        Register rd = cpu.getRegister(statement.getRd());
        Register ra = cpu.getRegister(RegisterType.RA);
        BitArray immediate = statement.getImmediate();
        int shamt = statement.getShamt();

        if (action instanceof RTypeAction) {
            RTypeAction rTypeAction = (RTypeAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                rTypeAction.execute(cpu, rs, rt, rd, shamt);
            };
        } else if (action instanceof ITypeAction) {
            ITypeAction iTypeAction = (ITypeAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                iTypeAction.execute(cpu, rs, rt, immediate);
            };
        } else if (action instanceof ConditionalAction) {
            ConditionalAction conditionalAction = (ConditionalAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                boolean check = conditionalAction.check(cpu, rs, rt);
                if (check) {
                    pc.set(pc.get() + statement.getOffset() * 4);
                }
            };
        } else if (action instanceof JumpAction) {
            JumpAction jumpAction = (JumpAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                pc.set(jumpAction.getNext(cpu, statement));
            };
        } else if (action instanceof MemoryAction) {
            MemoryAction memoryAction = (MemoryAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                memoryAction.execute(cpu, rs.getUnsigned() + immediate.integerValue(), rt);
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

    @Override
    public void onTick() {
        Platform.runLater(() -> {
            if (registerListener == null) return;
            for (Register register : registers) {
                registerListener.onRegisterChange(register);
            }
        });

    }
}
