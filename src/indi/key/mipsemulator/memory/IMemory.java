package indi.key.mipsemulator.memory;

import indi.key.mipsemulator.control.controller.Resetable;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;

public interface IMemory extends Resetable {
    void save(long address, byte[] bytes) throws MemoryOutOfBoundsException;

    byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException;
}
