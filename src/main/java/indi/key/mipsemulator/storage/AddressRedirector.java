package indi.key.mipsemulator.storage;

import java.io.File;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;

public class AddressRedirector extends AlternativeMemory {

    private Memory[] memories;
    private boolean init = false;

    public AddressRedirector() {
        memories = new Memory[MemoryType.VALUE.length];
        for (MemoryType memoryType : MemoryType.VALUE) {
            memories[memoryType.ordinal()] = memoryType.generateStorage();
        }
    }

    public boolean hasInit() {
        return init;
    }

    public void setInitFile(File initFile) {
        ((ByteArrayMemory) memories[MemoryType.RAM.ordinal()]).setInitFile(initFile);
        init = initFile != null;
    }

    public File getInitFile() {
        return ((ByteArrayMemory) memories[MemoryType.RAM.ordinal()]).getInitFile();
    }

    public Memory getMemory(MemoryType type) {
        return memories[type.ordinal()];
    }

    @Override
    protected int selectMemory(long address, boolean isRead, int length, MemorySelectedCallback callback, int params)
            throws MemoryOutOfBoundsException {
        boolean flag = true;
        int re = 0;
        for (MemoryType memoryType : MemoryType.VALUE) {
            int relative = memoryType.getRelativeAddress(address, length);
            if (relative >= 0) {
                flag = false;
                re = callback.onMemorySelected(memories[memoryType.ordinal()], relative, params);
            }
        }
        if (flag) {
            throw new MemoryOutOfBoundsException("Cannot access the address: 0x" +
                    Long.toHexString(address).toUpperCase());
        }
        return re;
    }

    @Override
    public void reset() {
        for (Memory memory : memories) {
            memory.reset();
        }
    }
}
