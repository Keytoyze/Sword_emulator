package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;

public interface IMemory extends Resetable {
    void save(long address, byte[] bytes) throws MemoryOutOfBoundsException;

    byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException;
}
