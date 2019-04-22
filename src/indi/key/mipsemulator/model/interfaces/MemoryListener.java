package indi.key.mipsemulator.model.interfaces;

import indi.key.mipsemulator.storage.Memory;

public interface MemoryListener {
    void onMemoryChange(Memory memory, int address, int length);
}
