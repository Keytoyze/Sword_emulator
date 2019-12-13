package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.interfaces.Resetable;

// Big-Endian
public interface Memory extends Resetable {

    void saveByte(long address, byte data) throws MemoryOutOfBoundsException;

    void saveHalf(long address, short data) throws MemoryOutOfBoundsException;

    void saveWord(long address, int data) throws MemoryOutOfBoundsException;

    byte loadByte(long address) throws MemoryOutOfBoundsException;

    short loadHalf(long address) throws MemoryOutOfBoundsException;

    int loadWord(long address) throws MemoryOutOfBoundsException;

    // This method ensures that the storage data will not be changed.
    byte loadByteConst(long address) throws MemoryOutOfBoundsException;

    default byte[] loadConst(long address, int length) throws MemoryOutOfBoundsException {
        byte[] result = new byte[length];
        long end = address + length;
        for (long i = address; i < end; i++) {
            result[(int) (i - address)] = loadByteConst(i);
        }
        return result;
    }
}
