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
                default:
                    content = IoUtils.read(initFile);
                    break;
            }
            System.arraycopy(content, 0, memory, 0, content.length);
        }
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
        try {
            int addrInt = (int) address;
            switch (bytes.length) {
                case 4:
                    memory[addrInt + 3] = bytes[3];
                    memory[addrInt + 2] = bytes[2];
                    // Fall
                case 2:
                    memory[addrInt + 1] = bytes[1];
                    // Fall
                case 1:
                    memory[addrInt] = bytes[0];
                    break;
                default:
                    System.arraycopy(bytes, 0, memory, addrInt, bytes.length);
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MemoryOutOfBoundsException("Cannot save address: " + Long.toHexString(address));
        }
    }

    @Override
    public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
        return loadConstantly(address, bytesNum);
    }

    @Override
    public byte[] loadConstantly(long address, int bytesNum) throws MemoryOutOfBoundsException {
        try {
            int addrInt = (int) address;
            switch (bytesNum) {
                case 4:
                    return new byte[] {memory[addrInt], memory[addrInt + 1], memory[addrInt + 2],
                            memory[addrInt + 3]};
                case 2:
                    return new byte[] {memory[addrInt], memory[addrInt + 1]};
                case 1:
                    return new byte[] {memory[addrInt]};
                default:
                    return Arrays.copyOfRange(memory, addrInt, addrInt + bytesNum);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MemoryOutOfBoundsException("Cannot load address 0x" + Long.toHexString(address)
                    + " out of limit: 0x" + Long.toHexString(memory.length));
        }
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
