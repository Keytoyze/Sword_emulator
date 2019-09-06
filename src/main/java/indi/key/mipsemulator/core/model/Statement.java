package indi.key.mipsemulator.core.model;

import indi.key.mipsemulator.disassemble.Dissambler;
import indi.key.mipsemulator.model.info.BitArray;


public class Statement {

    private BitArray value;
    private BitArray op;
    private BitArray rs;
    private BitArray rt;
    private BitArray rd;
    private BitArray shamt;
    private BitArray func;
    private BitArray immediate;
    private BitArray address;

    private Statement(int word) {
        this.value = BitArray.of(word, 32);
        this.op = value.subArray(26, 32);
        this.rs = value.subArray(21, 26);
        this.rt = value.subArray(16, 21);
        this.rd = value.subArray(11, 16);
        this.shamt = value.subArray(6, 11);
        this.func = value.subArray(0, 6);
        this.immediate = value.subArray(0, 16);
        this.address = value.subArray(0, 26);
    }

    public static Statement of(int value) {
        return new Statement(value);
    }

    public BitArray getValue() {
        return value;
    }

    public BitArray getOp() {
        return op;
    }

    public BitArray getRs() {
        return rs;
    }

    public BitArray getRt() {
        return rt;
    }

    public BitArray getRd() {
        return rd;
    }

    public BitArray getCofun() {
        return value.subArray(0, 25);
    }

    public BitArray getShamt() {
        return shamt;
    }

    public BitArray getFunc() {
        return func;
    }

    public BitArray getImmediate() {
        return immediate;
    }

    public int getOffset() {
        return immediate.integerValue();
    }

    public BitArray getAddress() {
        return address;
    }

    public Instruction getInstruction() {
        return Operation.of(op, rs, rt, func).toInstruction();
    }

    public Operation getOperation() {
        return Operation.of(op, rs, rt, func);
    }

    @Override
    public String toString() {
        return Dissambler.dissambleStatement(this);
    }
}
