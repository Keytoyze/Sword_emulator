package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;

public abstract class AlternativeMemory implements Memory {

    protected abstract int selectMemory(long address, boolean isRead, int length, MemorySelectedCallback callback, int param);

    @Override
    public void saveByte(long address, byte data) throws MemoryOutOfBoundsException {
        selectMemory(address, false, 1, MemorySelectedCallback.SAVE_BYTE, (int) data);
    }

    @Override
    public void saveHalf(long address, short data) throws MemoryOutOfBoundsException {
        selectMemory(address, false, 2, MemorySelectedCallback.SAVE_HALF, (int) data);
    }

    @Override
    public void saveWord(long address, int data) throws MemoryOutOfBoundsException {
        selectMemory(address, false, 4, MemorySelectedCallback.SAVE_WORD, data);
    }

    @Override
    public byte loadByte(long address) throws MemoryOutOfBoundsException {
        return (byte) selectMemory(address, true, 1, MemorySelectedCallback.LOAD_BYTE, 0);
    }

    @Override
    public short loadHalf(long address) throws MemoryOutOfBoundsException {
        return (short) selectMemory(address, true, 2, MemorySelectedCallback.LOAD_HALF, 0);
    }

    @Override
    public int loadWord(long address) throws MemoryOutOfBoundsException {
        return selectMemory(address, true, 4, MemorySelectedCallback.LOAD_WORD, 0);
    }

    @Override
    public byte loadByteConst(long address) throws MemoryOutOfBoundsException {
        return (byte) selectMemory(address, true, 4, MemorySelectedCallback.LOAD_CONST, 0);
    }

}
