package indi.key.mipsemulator.storage;

public class SplitRegisterMemory<T extends Memory> extends AlternativeMemory {

    private T writeMemory;
    private T readMemory;

    public void beforeLoad(T memory) {
    }

    public void onSave(T memory) {
    }

    @Override
    protected int selectMemory(long address, boolean isRead, int length, MemorySelectedCallback callback, int param) {
        T memory = isRead ? readMemory : writeMemory;
        if (isRead && callback != MemorySelectedCallback.LOAD_CONST) {
            beforeLoad(memory);
        }
        int re = callback.onMemorySelected(memory, address, param);
        if (!isRead) {
            onSave(memory);
        }
        return re;
    }

    SplitRegisterMemory(T writeMemory, T readMemory) {
        this.writeMemory = writeMemory;
        this.readMemory = readMemory;
    }

    @Override
    public void reset() {
        writeMemory.reset();
        readMemory.reset();
    }
}
