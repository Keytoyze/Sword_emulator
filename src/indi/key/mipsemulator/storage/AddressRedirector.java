package indi.key.mipsemulator.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.interfaces.MemoryListener;
import indi.key.mipsemulator.util.IoUtils;

public class AddressRedirector implements Memory {

    private Memory[] memories;
    private ArrayList<ArrayList<MemoryListener>> listeners;
    private boolean init = false;

    public AddressRedirector(File initFile) {
        memories = new Memory[MemoryType.values().length];
        listeners = new ArrayList<>(MemoryType.values().length);
        for (MemoryType memoryType : MemoryType.values()) {
            memories[memoryType.ordinal()] = memoryType.generateStorage();
            listeners.add(memoryType.ordinal(), new ArrayList<>());
        }
        setInitFile(initFile);
    }

    public boolean hasInit() {
        return init;
    }

    public void setInitFile(File initFile) {
        ((ByteArrayMemory) memories[MemoryType.RAM.ordinal()]).setInitFile(initFile);
        init = initFile != null;
    }

    public void addListener(MemoryType listenTo, MemoryListener listener) {
        listeners.get(listenTo.ordinal()).add(listener);
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        selectMemory(address, bytes.length, (memoryType, relativeAddress) -> {
            Memory memory = memories[memoryType.ordinal()];
            boolean notify = false;
            if (!Arrays.equals(memory.loadConstantly(relativeAddress, bytes.length), bytes)) {
                notify = true;
            }
            memory.save(relativeAddress, bytes);
            if (notify) {
                ArrayList<MemoryListener> memoryListeners = listeners.get(memoryType.ordinal());
                final int size = memoryListeners.size();
                for (int i = 0; i < size; i++) {
                    memoryListeners.get(i).onMemoryChange(memory, relativeAddress, bytes.length);
                }
            }
            return null;
        });
    }

    public void saveInt(long address, int data) {
        save(address, IoUtils.intToBytes(data, 32));
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        return selectMemory(address, bytesNum, (memoryType, relativeAddress) -> {
            Memory memory = memories[memoryType.ordinal()];
            return memory.load(relativeAddress, bytesNum);
        });
    }

    public int loadInt(long address) {
        return IoUtils.bytesToInt(load(address, 4));
    }

    @Override
    public byte[] loadConstantly(long address, int bytesNum) throws MemoryOutOfBoundsException {
        return selectMemory(address, bytesNum, (memoryType, relativeAddress) -> {
            Memory memory = memories[memoryType.ordinal()];
            return memory.loadConstantly(relativeAddress, bytesNum);
        });
    }

    public Memory getMemory(MemoryType type) {
        return memories[type.ordinal()];
    }

    private byte[] selectMemory(long address, int length, MemorySelectedCallback callback)
            throws MemoryOutOfBoundsException {
        boolean flag = true;
        byte[] re = null;
        for (MemoryType memoryType : MemoryType.values()) {
            int relative = memoryType.getRelativeAddress(address, length);
            if (relative >= 0) {
                flag = false;
                re = callback.onMemorySelected(memoryType, relative);
            }
        }
        if (flag) {
            throw new MemoryOutOfBoundsException("Cannot access the address: 0x" +
                    Long.toHexString(address));
        }
        return re;
    }

    @Override
    public void reset() {
        for (Memory memory : memories) {
            memory.reset();
        }
    }

    private interface MemorySelectedCallback {
        byte[] onMemorySelected(MemoryType memoryType, int relativeAddress);
    }
}
