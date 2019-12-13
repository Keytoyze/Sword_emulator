package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.model.exception.EmulatorException;
import indi.key.mipsemulator.model.exception.ModifyZeroException;
import indi.key.mipsemulator.model.interfaces.Resetable;

public class Register implements Comparable<Register>, Resetable {

    private RegisterType registerType;
    private int value;

    public Register(RegisterType registerType) {
        this.registerType = registerType;
        this.value = 0;
    }

    public void set(int value) throws EmulatorException {
        checkZero(value);
        setInternal(value);
    }

    public void setUnsigned(long value) throws EmulatorException {
        checkZero((int) value);
        setInternal((int) value);
    }

    private void setInternal(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public RegisterType getRegisterType() {
        return registerType;
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

    public String getHex() {
        return Integer.toHexString(value);
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
        return ((Register) obj).value == value;
    }

    @Override
    public int compareTo(Register o) {
        return Integer.compare(value, o.value);
    }

    @Override
    public String toString() {
        return registerType.toString() + ": " + value;
    }
}
