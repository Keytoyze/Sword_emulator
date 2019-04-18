package indi.key.mipsemulator.core.model;

import indi.key.mipsemulator.core.action.Action;
import indi.key.mipsemulator.core.action.ConditionalAction;
import indi.key.mipsemulator.core.action.ITypeAction;
import indi.key.mipsemulator.core.action.RTypeAction;
import indi.key.mipsemulator.storage.RegisterType;
import indi.key.mipsemulator.model.bean.BitArray;


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

    public RegisterType getRs() {
        return RegisterType.of(rs.value());
    }

    public RegisterType getRt() {
        return RegisterType.of(rt.value());
    }

    public RegisterType getRd() {
        return RegisterType.of(rd.value());
    }

    public int getShamt() {
        return shamt.value();
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

    public int getAddress() {
        return address.value();
    }

    public Instruction getInstruction() {
        return Operation.of(op, rs, rt, func).toInstruction();
    }

    @Override
    public String toString() {
        // TODO: translate to assembly language
        Instruction instruction = getInstruction();
        StringBuilder stringBuilder = new StringBuilder(instruction.toString());
        Action action = instruction.getAction();
        if (action instanceof RTypeAction) {
            stringBuilder.append(" ").append(getRd().toString())
                    .append(" ").append(getRs().toString())
                    .append(" ").append(getRt().toString());
        } else if (action instanceof ITypeAction) {
            stringBuilder.append(" ").append(getRs().toString())
                    .append(" ").append(getRt().toString())
                    .append(" ").append(immediate.integerValue());
        } else if (action instanceof ConditionalAction) {
            stringBuilder.append(" ").append(getRs().toString())
                    .append(" ").append(getRt().toString())
                    .append(" ").append(getOffset());
        } else {
            stringBuilder.append(" ").append(getRd().toString())
                    .append(" ").append(getRs().toString())
                    .append(" ").append(getRt().toString());
        }
        return stringBuilder.toString();
    }
}
