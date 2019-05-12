package indi.key.mipsemulator.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.Range;
import indi.key.mipsemulator.model.interfaces.MemoryListener;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.vga.VgaConfigures;
import javafx.util.Pair;

public class AddressRedirector implements Memory {

    private Memory[] memories;
    private ArrayList<List<MemoryListener>> listeners;

    public AddressRedirector(File initFile) {
        memories = new Memory[MemoryType.values().length];
        listeners = new ArrayList<>(MemoryType.values().length);
        for (MemoryType memoryType : MemoryType.values()) {
            memories[memoryType.ordinal()] = memoryType.generateStorage();
            listeners.add(memoryType.ordinal(), new LinkedList<>());
        }
        setInitFile(initFile);
    }

    public void setInitFile(File initFile) {
        ((ByteArrayMemory) memories[MemoryType.RAM.ordinal()]).setInitFile(initFile);
    }

    public void addListener(MemoryType listenTo, MemoryListener listener) {
        listeners.get(listenTo.ordinal()).add(listener);
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        Pair<MemoryType, Integer> memoryPair = selectMemory(new Range<>(address, address + bytes.length - 1));
        Memory memory = memories[memoryPair.getKey().ordinal()];
        memory.save(memoryPair.getValue(), bytes);
        for (MemoryListener listener : listeners.get(memoryPair.getKey().ordinal())) {
            listener.onMemoryChange(memory, memoryPair.getValue(), bytes.length);
        }
    }

    public void saveInt(long address, int data) {
        save(address, IoUtils.intToBytes(data, 32));
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        Pair<MemoryType, Integer> memoryPair = selectMemory(new Range<>(address, address + bytesNum - 1));
        Memory memory = memories[memoryPair.getKey().ordinal()];
        return memory.load(memoryPair.getValue(), bytesNum);
    }

    public int loadInt(long address) {
        return IoUtils.bytesToInt(load(address, 4));
    }

    public Memory getMemory(MemoryType type) {
        return memories[type.ordinal()];
    }

    private Pair<MemoryType, Integer> selectMemory(Range<Long> dataRange) throws MemoryOutOfBoundsException {
        for (MemoryType memoryType : MemoryType.values()) {
            if (memoryType == MemoryType.VRAM_GRAPH || memoryType == MemoryType.VRAM_TEXT) {
                int relative = memoryType.getRelativeAddress(dataRange, VgaConfigures.getAddressOffset());
                if (relative >= 0) {
                    return new Pair<>(memoryType, relative);
                }
            } else {
                int relative = memoryType.getRelativeAddress(dataRange);
                if (relative >= 0) {
                    return new Pair<>(memoryType, relative);
                }
            }
        }
        throw new MemoryOutOfBoundsException("Cannot access the address " + dataRange.toString());
    }

    @Override
    public void reset() {
        for (Memory memory : memories) {
            memory.reset();
        }
    }
}
