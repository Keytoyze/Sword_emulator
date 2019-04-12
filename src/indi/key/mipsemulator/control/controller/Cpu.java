package indi.key.mipsemulator.control.controller;

import java.io.File;

import indi.key.mipsemulator.control.model.Action;
import indi.key.mipsemulator.control.model.ConditionalAction;
import indi.key.mipsemulator.control.model.ITypeAction;
import indi.key.mipsemulator.control.model.Instruction;
import indi.key.mipsemulator.control.model.JTypeAction;
import indi.key.mipsemulator.control.model.JumpAction;
import indi.key.mipsemulator.control.model.RTypeAction;
import indi.key.mipsemulator.control.model.Register;
import indi.key.mipsemulator.control.model.RegisterType;
import indi.key.mipsemulator.memory.AddressRedirector;
import indi.key.mipsemulator.memory.MemoryType;
import indi.key.mipsemulator.model.exception.NotImplementedException;
import indi.key.mipsemulator.model.BitArray;

public class Cpu implements Resetable {

    private Register[] registers;
    private AddressRedirector addressRedirector;

    // For looping
    private boolean looping;
    private long startTime;
    private int instructionCount;
    private int errorCount;

    private Runnable[] instructionCache;

    public Cpu(File initFile) {
        this.addressRedirector = new AddressRedirector(initFile);
        registers = new Register[RegisterType.values().length];
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register(RegisterType.of(i));
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


    public Register getHI() {
        return registers[RegisterType.HI.ordinal()];
    }

    public Register getLO() {
        return registers[RegisterType.LO.ordinal()];
    }

    public byte[] loadMemory(int address, int bytesNum) {
        return addressRedirector.load(address, bytesNum);
    }

    public int loadInt(int address) {
        return addressRedirector.loadInt(address);
    }

    public void saveMemory(int address, byte[] data) {
        addressRedirector.save(address, data);
    }

    public void saveInt(int address, int data) {
        addressRedirector.saveInt(address, data);
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
    }

    public CpuStatistics exitLoop() {
        looping = false;
        return new CpuStatistics(System.currentTimeMillis() - startTime, instructionCount, errorCount);
    }

    private static Runnable getStatementRunnable(Cpu cpu) {
        Register pc = cpu.getRegister(RegisterType.PC);
        int index = pc.get();
        //System.out.println(pc.get() / 4 + 1 + " " + statement.toString());
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
        int address = statement.getAddress();


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
        } else if (action instanceof JTypeAction) {
            JTypeAction jTypeAction = (JTypeAction) action;
            return () -> {
                cpu.track(index, statement);
                beforeExcution(pc, ra, linkNext);
                jTypeAction.execute(cpu, address);
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
        } else {
            return () -> {
                throw new NotImplementedException("Action " + instruction.name() + " haven't been implemented.");
            };
        }
    }

    private void track(int index, Statement statement) {

    }

    private static void beforeExcution(Register pc, Register ra, boolean linkNext) {
        // Move pc to loop;
        pc.set(pc.get() + 4);
        // Link the address to $ra
        if (linkNext) {
            ra.set(pc.get());
        }
    }
}
