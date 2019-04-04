package indi.key.mipsemulator.model.storage;

import java.io.File;
import java.util.Arrays;

import indi.key.mipsemulator.controller.emulator.Resetable;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.util.BitArray;
import indi.key.mipsemulator.util.IoUtils;

public abstract class BaseMemory implements Resetable {

    private File raw;
    private int depth;
    byte[] memory;

    BaseMemory(File file) {
        this.raw = file;
    }

    BaseMemory(int depth) {
        this.raw = null;
        this.depth = depth;
    }

    @Override
    public void reset() {
        if (raw == null) {
            this.memory = new byte[depth];
        } else {
            this.memory = IoUtils.read(raw);
            this.depth = memory.length;
        }
    }

    public int getDepth() {
        return depth;
    }

    public BitArray load(int address, int byteNum) throws MemoryOutOfBoundsException {
        checkBounds(address + byteNum - 1);
        return BitArray.of(Arrays.copyOfRange(memory, address, address + byteNum));
    }

    void checkBounds(int address) throws MemoryOutOfBoundsException {
        if (address >= memory.length) {
            throw new MemoryOutOfBoundsException("Try to access address " + address
                    + " of " + getType() + " (depth = " + memory.length + ")");
        } else if (address < 0) {
            throw new MemoryOutOfBoundsException("Try to access negative address " + address
                    + " of " + getType() + " (depth = " + memory.length + ")");
        }
    }

    public abstract String getType();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < memory.length; i++) {
            stringBuilder.append(Integer.toHexString(memory[i])).append(" ");
            if (i % 5 == 4) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
