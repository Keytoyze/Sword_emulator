package indi.key.mipsemulator.storage;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import indi.key.mipsemulator.disassemble.CoeReader;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.util.IoUtils;

public class ByteArrayMemory implements Memory {

    private byte[] memory;
    private int depth;
    private File initFile = null;

    public ByteArrayMemory(int depth) {
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
            String fileName = initFile.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            byte[] content;
            switch (suffix) {
                case "coe":
                    try (FileInputStream fileInputStream = new FileInputStream(initFile)) {
                        content = CoeReader.coeToBytes(fileInputStream);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "bin":
                    content = IoUtils.read(initFile);
                    break;
                default:
                    throw new RuntimeException("Unknown suffix: " + suffix);
            }
            System.arraycopy(content, 0, memory, 0, content.length);
        }
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        System.arraycopy(bytes, 0, memory, (int) address, bytes.length);
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        return Arrays.copyOfRange(memory, (int) address, (int) address + bytesNum);
    }

    protected byte[] getAll() {
        return memory;
    }

    //    void checkBounds(int address) throws MemoryOutOfBoundsException {
////        if (address >= storage.length) {
////            throw new MemoryOutOfBoundsException("Try to access address " + address
////                    + " of " + getType() + " (depth = " + storage.length + ")");
////        } else if (address < 0) {
////            throw new MemoryOutOfBoundsException("Try to access negative address " + address
////                    + " of " + getType() + " (depth = " + storage.length + ")");
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
