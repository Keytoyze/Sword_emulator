package indi.key.mipsemulator.storage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import indi.key.mipsemulator.model.bean.Range;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.util.IoUtils;
import javafx.util.Pair;

public class AddressRedirector implements IMemory {

    private Map<MemoryType, IMemory> memoryMap;

    public AddressRedirector(File initFile) {
        memoryMap = new HashMap<>(MemoryType.values().length);
        for (MemoryType memoryType : MemoryType.values()) {
            memoryMap.put(memoryType, memoryType.generateStorage());
        }
        ((ByteArrayMemory) memoryMap.get(MemoryType.RAM)).setInitFile(initFile);
    }


    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        Pair<IMemory, Integer> memoryPair = selectMemory(new Range<>(address, address + bytes.length - 1));
        memoryPair.getKey().save(memoryPair.getValue(), bytes);
    }

    public void saveInt(long address, int data) {
        save(address, IoUtils.intToBytes(data, 32));
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        Pair<IMemory, Integer> memoryPair = selectMemory(new Range<>(address, address + bytesNum - 1));
        return memoryPair.getKey().load(memoryPair.getValue(), bytesNum);
    }

    public int loadInt(long address) {
        return IoUtils.bytesToInt(load(address, 4));
    }

    private Pair<IMemory, Integer> selectMemory(Range<Long> dataRange) throws MemoryOutOfBoundsException {
        for (Map.Entry<MemoryType, IMemory> entry : memoryMap.entrySet()) {
            MemoryType memoryType = entry.getKey();
            if (memoryType == MemoryType.VRAM_GRAPH || memoryType == MemoryType.VRAM_TEXT) {
                // TODO: remove magical number
                if (memoryType.contains(dataRange, 0x000C0000)) {
                    return new Pair<>(entry.getValue(),
                            memoryType.getRelativeAddress(dataRange.getStart()) - 0x000C0000);
                }
            } else {
                if (memoryType.contains(dataRange)) {
                    return new Pair<>(entry.getValue(),
                            memoryType.getRelativeAddress(dataRange.getStart()));
                }
            }
        }
        throw new MemoryOutOfBoundsException("Cannot assess the address " + dataRange.toString());
    }

    @Override
    public void reset() {
        for (Map.Entry<MemoryType, IMemory> entry : memoryMap.entrySet()) {
            entry.getValue().reset();
        }
    }
}
