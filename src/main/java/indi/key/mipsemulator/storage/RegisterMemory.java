package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.util.IoUtils;

public class RegisterMemory implements Memory {

    private BitArray content = BitArray.ofLength(32);
    private final static BitArray CACHE = BitArray.ofEmpty();

    public BitArray getBitArray() {
        return content;
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        if (address < 0 || address + bytes.length > 4) {
            throw new MemoryOutOfBoundsException("Memory out of bound: 0x" + Long.toHexString(address));
        }
        content.setTo((int) address * 8, IoUtils.bytesToInt(bytes), bytes.length * 8);
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        if (address < 0 || address + bytesNum > 4) {
            throw new MemoryOutOfBoundsException("Memory out of bound: 0x" + Long.toHexString(address));
        }
        int index = (int) address * 8;
        synchronized (CACHE) {
            content.subArray(index, index + bytesNum * 8, CACHE);
            return CACHE.bytes();
        }
    }

    @Override
    public byte[] loadConstantly(long address, int bytesNum) throws MemoryOutOfBoundsException {
        return load(address, bytesNum);
    }

    @Override
    public void reset() {
        content.setTo(0, 32);
    }
}
