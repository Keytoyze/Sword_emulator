package indi.key.mipsemulator.core.model;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.disassemble.Dissambler;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.storage.Register;

public final class Statement {

    public final BitArray value;
    public final BitArray op, rs, rt, rd, shamt, func, immediate, address, cofun;
    public final Operation operation;
    public final Instruction instruction;
    public final Register rsReg, rtReg, rdReg;

    private Statement(int word, Machine machine) {
        this.value = BitArray.of(word, 32);
        // decode every elements here.
        this.op = value.subArray(26, 32);
        this.rs = value.subArray(21, 26);
        this.rt = value.subArray(16, 21);
        this.rd = value.subArray(11, 16);
        this.shamt = value.subArray(6, 11);
        this.func = value.subArray(0, 6);
        this.immediate = value.subArray(0, 16);
        this.address = value.subArray(0, 26);
        this.cofun = value.subArray(0, 25);
        this.operation = Operation.of(op, rs, rt, func);
        this.instruction = operation.toInstruction();
        this.rsReg = machine.getRegister(rs);
        this.rtReg = machine.getRegister(rt);
        this.rdReg = machine.getRegister(rd);
    }

    public static Statement of(int value) {
        return new Statement(value, Machine.getInstance());
    }

    @Override
    public String toString() {
        return Dissambler.dissambleStatement(this);
    }
}
