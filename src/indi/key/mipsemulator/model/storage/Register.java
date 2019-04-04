package indi.key.mipsemulator.model.storage;

import indi.key.mipsemulator.controller.emulator.Resetable;
import indi.key.mipsemulator.model.exception.EmulatorException;
import indi.key.mipsemulator.model.exception.ModifyZeroException;

public class Register implements Comparable<Register>, Resetable {

    private RegisterType registerType;
    private Integer value;

    public Register(RegisterType registerType) {
        this.registerType = registerType;
        this.value = 0;
    }

    public void set(int value) throws EmulatorException {
        checkZero(value);
        this.value = value;
    }

    public void setUnsigned(long value) throws EmulatorException {
        checkZero((int) value);
        this.value = (int) value;
    }

    public int get() {
        return value;
    }

    @Override
    public void reset() {
        this.value = 0;
    }

    public long getAsLong() {
        return (long) value;
    }

    public long getUnsigned() {
        return Integer.toUnsignedLong(value);
    }

    private void checkZero(int value) {
        if (registerType == RegisterType.ZERO && value != 0) {
            throw new ModifyZeroException("You cannot modify ZERO registerType! ");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Register)) {
            return false;
        }
        return ((Register) obj).value.equals(value);
    }

    @Override
    public int compareTo(Register o) {
        return value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return registerType.toString() + ": " + value;
    }
}
