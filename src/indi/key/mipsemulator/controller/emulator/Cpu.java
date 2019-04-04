package indi.key.mipsemulator.controller.emulator;

import indi.key.mipsemulator.model.storage.Ram;
import indi.key.mipsemulator.model.storage.Register;
import indi.key.mipsemulator.model.storage.RegisterType;
import indi.key.mipsemulator.model.storage.Rom;
import indi.key.mipsemulator.model.exception.NotImplementedException;
import indi.key.mipsemulator.controller.instruction.Action;
import indi.key.mipsemulator.controller.instruction.ConditionalAction;
import indi.key.mipsemulator.controller.instruction.ITypeAction;
import indi.key.mipsemulator.controller.instruction.Instruction;
import indi.key.mipsemulator.controller.instruction.JTypeAction;
import indi.key.mipsemulator.controller.instruction.JumpAction;
import indi.key.mipsemulator.controller.instruction.RTypeAction;

public class Cpu implements Resetable {

    private Register[] registers;
    private Ram ram;
    private Rom rom;

    public Cpu(Ram ram, Rom rom) {
        this.ram = ram;
        this.rom = rom;
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
        this.ram.reset();
        this.rom.reset();
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

    public Ram getRam() {
        return ram;
    }

    public void execute() {
        Register pc = getRegister(RegisterType.PC);
        Statement statement = Statement.of(rom.load(pc.get(), 4));

        Instruction instruction = statement.getInstruction();
        Action action = instruction.getAction();
        boolean linkNext = instruction.isLinkNext();

        // Move pc to next;
        pc.set(pc.get() + 4);
        // Link the address to $ra
        if (linkNext) {
            getRegister(RegisterType.RA).set(pc.get());
        }

        if (action instanceof RTypeAction) {
            ((RTypeAction) action).execute(this,
                    getRegister(statement.getRs()),
                    getRegister(statement.getRt()),
                    getRegister(statement.getRd()), statement.getShamt());
        } else if (action instanceof ITypeAction) {
            ((ITypeAction) action).execute(this,
                    getRegister(statement.getRs()),
                    getRegister(statement.getRt()), statement.getImmediate());
        } else if (action instanceof JTypeAction) {
            ((JTypeAction) action).execute(this, statement.getAddress());
        } else if (action instanceof ConditionalAction) {
            boolean check = ((ConditionalAction) action).check(this,
                    getRegister(statement.getRs()),
                    getRegister(statement.getRt()));
            if (check) {
                pc.set(pc.get() + statement.getOffset() * 4);
            }
        } else if (action instanceof JumpAction) {
            pc.set(((JumpAction)action).getNext(this, statement));
        } else {
            throw new NotImplementedException("Action " + instruction.name() + " haven't been implemented.");
        }
    }
}
