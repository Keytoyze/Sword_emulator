package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;

public class GpioRegister implements Memory {

    public GpioRegister(int length) {

    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {

    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        return new byte[0];
    }

    @Override
    public void reset() {

    }
}
