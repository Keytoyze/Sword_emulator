package indi.key.mipsemulator.storage;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import indi.key.mipsemulator.model.bean.Range;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.LogUtils;

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
        Map.Entry<MemoryType, IMemory> entry = selectMemory(new Range<>(address, address + bytes.length - 1));
        entry.getValue().save(entry.getKey().getRelativeAddress(address), bytes);
    }

    public void saveInt(long address, int data) {
        LogUtils.i(Arrays.toString(IoUtils.intToBytes(data, 32)));
        save(address, IoUtils.intToBytes(data, 32));
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        Map.Entry<MemoryType, IMemory> entry = selectMemory(new Range<>(address, address + bytesNum - 1));
        return entry.getValue().load(entry.getKey().getRelativeAddress(address), bytesNum);
    }

    public int loadInt(long address) {
        return IoUtils.bytesToInt(load(address, 4));
    }

    private Map.Entry<MemoryType, IMemory> selectMemory(Range<Long> dataRange) throws MemoryOutOfBoundsException {
        for (Map.Entry<MemoryType, IMemory> entry : memoryMap.entrySet()) {
            MemoryType memoryType = entry.getKey();
            boolean check;
            if (memoryType == MemoryType.VRAM_GRAPH || memoryType == MemoryType.VRAM_TEXT) {
                // TODO: remove magical number
                check = memoryType.contains(dataRange, 0x000C0000);
            } else {
                check = memoryType.contains(dataRange);
            }
            if (check) {
                return entry;
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
