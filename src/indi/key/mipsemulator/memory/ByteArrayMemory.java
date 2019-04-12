package indi.key.mipsemulator.memory;

import java.io.File;
import java.util.Arrays;

import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.LogUtils;

public class ByteArrayMemory implements IMemory {

    private byte[] memory;
    private int depth;
    private File initFile = null;

    ByteArrayMemory(int depth) {
        this.depth = depth;
        reset();
    }

    void setInitFile(File initFile) {
        this.initFile = initFile;
        reset();
    }

    @Override
    public void reset() {
        memory = new byte[depth];
        if (initFile != null) {
            byte[] content = IoUtils.read(initFile);
            System.arraycopy(content, 0, memory, 0, content.length);
        }
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        LogUtils.i(address + " " + bytes.length + " " + memory.length);
        System.arraycopy(bytes, 0, memory, (int) address, bytes.length);
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        return Arrays.copyOfRange(memory, (int) address, (int) address + bytesNum);
    }

    //    void checkBounds(int address) throws MemoryOutOfBoundsException {
////        if (address >= memory.length) {
////            throw new MemoryOutOfBoundsException("Try to access address " + address
////                    + " of " + getType() + " (depth = " + memory.length + ")");
////        } else if (address < 0) {
////            throw new MemoryOutOfBoundsException("Try to access negative address " + address
////                    + " of " + getType() + " (depth = " + memory.length + ")");
////        }
////    }

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
