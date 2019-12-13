package indi.key.mipsemulator.storage;

public interface MemorySelectedCallback {
    int onMemorySelected(Memory memory, long address, int param);

    MemorySelectedCallback LOAD_BYTE = (memory, address, param) -> (int) memory.loadByte(address);
    MemorySelectedCallback LOAD_HALF = (memory, address, param) -> (int) memory.loadHalf(address);
    MemorySelectedCallback LOAD_WORD = (memory, address, param) -> memory.loadWord(address);
    MemorySelectedCallback LOAD_CONST = (memory, address, param) -> (int) memory.loadByteConst(address);
    MemorySelectedCallback SAVE_BYTE = (memory, address, param) -> {
        memory.saveByte(address, (byte) param);
        return 0;
    };
    MemorySelectedCallback SAVE_HALF = (memory, address, param) -> {
        memory.saveHalf(address, (short) param);
        return 0;
    };
    MemorySelectedCallback SAVE_WORD = (memory, address, param) -> {
        memory.saveWord(address, param);
        return 0;
    };
}