package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;

public class GpioRegister implements Memory {

    private ByteArrayMemory readRegister;
    private ByteArrayMemory writeRegister;

    public GpioRegister(int length) {
        this.readRegister = new ByteArrayMemory(4);
        this.writeRegister = new ByteArrayMemory(4);
        reset();
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        // TODO: change cursor
        Machine machine = Machine.getReference();
        writeRegister.save(address, bytes);
        BitArray bitArray = BitArray.of(writeRegister.getAll());
        machine.setLed(bitArray.subArray(2, 18));
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        Machine machine = Machine.getReference();
        BitArray bitArray = BitArray.of(readRegister.getAll());
        bitArray.set(31, machine.getCounter().getCounterOut());
        bitArray.setTo(0, machine.getSwitches());
        readRegister.save(0, bitArray.bytes());
        return readRegister.load(address, bytesNum);
    }

    @Override
    public void reset() {
        readRegister.reset();
        writeRegister.reset();
    }
}
