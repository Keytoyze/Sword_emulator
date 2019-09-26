package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;

public class GpioRegister implements Memory {

    private RegisterMemory readRegister;
    private RegisterMemory writeRegister;

    private BitArray ledData = BitArray.ofEmpty();

    public GpioRegister(int length) {
        this.readRegister = new RegisterMemory();
        this.writeRegister = new RegisterMemory();
        reset();
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        // TODO: change cursor
        Machine machine = Machine.getInstance();
        writeRegister.save(address, bytes);
        writeRegister.getBitArray().subArray(2, 18, ledData);
        machine.setLed(ledData);
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        Machine machine = Machine.getInstance();
        BitArray bitArray = readRegister.getBitArray();
        boolean counterOut = machine.getCounter().getCounterOut();
        bitArray.set(31, counterOut);
        bitArray.set(30, counterOut);
        bitArray.set(29, counterOut);
        bitArray.setTo(16, machine.getButtons());
        bitArray.setTo(0, machine.getSwitches());

        return readRegister.load(address, bytesNum);
    }

    @Override
    public byte[] loadConstantly(long address, int bytesNum) throws MemoryOutOfBoundsException {
        return readRegister.load(address, bytesNum);
    }

    @Override
    public void reset() {
        readRegister.reset();
        writeRegister.reset();
    }
}
