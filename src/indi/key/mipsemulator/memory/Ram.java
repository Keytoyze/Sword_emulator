package indi.key.mipsemulator.memory;

import java.io.File;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.util.BitArray;

public class Ram extends BaseMemory {

    public Ram(File file) {
        super(file);
    }

    public Ram(int depth) {
        super(depth);
    }

    private void save(int address, byte[] value) throws MemoryOutOfBoundsException {
        checkBounds(address + value.length - 1);
        System.arraycopy(value, 0, memory, address, value.length);
    }

    public void saveWord(int address, int value) throws MemoryOutOfBoundsException {
        save(address, BitArray.ofValue(value).bytes());
    }

    public void saveHalf(int address, int value) throws MemoryOutOfBoundsException {
        save(address, BitArray.ofValue(value).setLength(16).bytes());
    }

    public void saveByte(int address, byte value) throws MemoryOutOfBoundsException {
        save(address, new byte[]{value});
    }

    @Override
    public String getType() {
        return "RAM";
    }
}
