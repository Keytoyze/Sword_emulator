package indi.key.mipsemulator.storage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import indi.key.mipsemulator.model.bean.Range;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.interfaces.MemoryListener;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.LogUtils;
import javafx.util.Pair;

public class AddressRedirector implements Memory {

    private Map<MemoryType, Memory> memoryMap;
    private Map<MemoryType, MemoryListener> listenerMap;

    public AddressRedirector(File initFile) {
        memoryMap = new HashMap<>(MemoryType.values().length);
        listenerMap = new HashMap<>(MemoryType.values().length);
        for (MemoryType memoryType : MemoryType.values()) {
            memoryMap.put(memoryType, memoryType.generateStorage());
        }
        ((ByteArrayMemory) memoryMap.get(MemoryType.RAM)).setInitFile(initFile);
    }

    public void setListener(MemoryType listenTo, MemoryListener listener) {
        listenerMap.put(listenTo, listener);
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        Pair<MemoryType, Integer> memoryPair = selectMemory(new Range<>(address, address + bytes.length - 1));
        Memory memory = memoryMap.get(memoryPair.getKey());
        if (memoryPair.getKey() == MemoryType.VRAM_GRAPH) {
            LogUtils.i(Long.toHexString(address));
        }
        memory.save(memoryPair.getValue(), bytes);
        if (listenerMap.containsKey(memoryPair.getKey())) {
            listenerMap.get(memoryPair.getKey()).onMemoryChange(memory, memoryPair.getValue(), bytes.length);
        }
    }

    public void saveInt(long address, int data) {
        save(address, IoUtils.intToBytes(data, 32));
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        Pair<MemoryType, Integer> memoryPair = selectMemory(new Range<>(address, address + bytesNum - 1));
        Memory memory = memoryMap.get(memoryPair.getKey());
        return memory.load(memoryPair.getValue(), bytesNum);
    }

    public int loadInt(long address) {
        return IoUtils.bytesToInt(load(address, 4));
    }

    public Memory getMemory(MemoryType type) {
        return memoryMap.get(type);
    }

    private Pair<MemoryType, Integer> selectMemory(Range<Long> dataRange) throws MemoryOutOfBoundsException {
        for (Map.Entry<MemoryType, Memory> entry : memoryMap.entrySet()) {
            MemoryType memoryType = entry.getKey();
            if (memoryType == MemoryType.VRAM_GRAPH || memoryType == MemoryType.VRAM_TEXT) {
                // TODO: remove magical number
                if (memoryType.contains(dataRange, 0x000C3F00)) {
                    return new Pair<>(memoryType,
                            memoryType.getRelativeAddress(dataRange.getStart()) - 0x000C3F00);
                }
            } else {
                if (memoryType.contains(dataRange)) {
                    return new Pair<>(memoryType,
                            memoryType.getRelativeAddress(dataRange.getStart()));
                }
            }
        }
        throw new MemoryOutOfBoundsException("Cannot assess the address " + dataRange.toString());
    }

    @Override
    public void reset() {
        for (Map.Entry<MemoryType, Memory> entry : memoryMap.entrySet()) {
            entry.getValue().reset();
        }
    }
}
