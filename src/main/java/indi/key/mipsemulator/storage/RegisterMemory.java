package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;

public class RegisterMemory implements Memory {

    protected BitArray content = BitArray.ofLength(32);

    public BitArray getBitArray() {
        return content;
    }

    protected void beforeLoad() {
    }

    @Override
    public void saveByte(long address, byte data) throws MemoryOutOfBoundsException {
        checkOutOfBound(address, 1);
        content.setTo((int) address * 8, ((int) data & 0xff), 8);
    }

    @Override
    public void saveHalf(long address, short data) throws MemoryOutOfBoundsException {
        checkOutOfBound(address, 2);
        content.setTo((int) address * 8, ((int) data & 0xffff), 16);
    }

    @Override
    public void saveWord(long address, int data) throws MemoryOutOfBoundsException {
        checkOutOfBound(address, 4);
        content.setTo((int) address * 8, data, 32);
    }

    @Override
    public byte loadByte(long address) throws MemoryOutOfBoundsException {
        beforeLoad();
        return loadByteConst(address);
    }

    @Override
    public short loadHalf(long address) throws MemoryOutOfBoundsException {
        beforeLoad();
        return (short) ((content.value() >>> (address * 8)) & 0xffff);
    }

    @Override
    public int loadWord(long address) throws MemoryOutOfBoundsException {
        beforeLoad();
        return content.value() >>> (address * 8);
    }

    @Override
    public byte loadByteConst(long address) throws MemoryOutOfBoundsException {
        return (byte) ((content.value() >>> (address * 8)) & 0xff);
    }

    private void checkOutOfBound(long address, int length) {
        if (address < 0 || address + length > 4) {
            throw new MemoryOutOfBoundsException("Memory out of bound: 0x" + Long.toHexString(address));
        }
    }

    @Override
    public void reset() {
        content.setTo(0, 32);
    }
}
