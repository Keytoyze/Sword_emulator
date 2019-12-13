package indi.key.mipsemulator.storage;

import java.io.File;
import java.io.FileInputStream;

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

    public void setInitFile(File initFile) {
        this.initFile = initFile;
        reset();
    }

    public File getInitFile() {
        return this.initFile;
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
                case "hex":
                    try (FileInputStream fileInputStream = new FileInputStream(initFile)) {
                        content = CoeReader.hexToBytes(fileInputStream);
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

    protected void onSave(long address, int length) {
    }

    @Override
    public void saveByte(long address, byte data) throws MemoryOutOfBoundsException {
        try {
            memory[(int) address] = data;
            onSave(address, 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throwOutOfBoundException(address);
        }
    }

    @Override
    public void saveHalf(long address, short data) throws MemoryOutOfBoundsException {
        try {
            memory[(int) address] = (byte) ((data >> 8) & 0xff);
            memory[(int) address + 1] = (byte) (data & 0xff);
            onSave(address, 2);
        } catch (ArrayIndexOutOfBoundsException e) {
            throwOutOfBoundException(address);
        }
    }

    @Override
    public void saveWord(long address, int data) throws MemoryOutOfBoundsException {
        try {
            memory[(int) address] = (byte) ((data >> 24) & 0xff);
            memory[(int) address + 1] = (byte) ((data >> 16) & 0xff);
            memory[(int) address + 2] = (byte) ((data >> 8) & 0xff);
            memory[(int) address + 3] = (byte) (data & 0xff);
            onSave(address, 4);
        } catch (ArrayIndexOutOfBoundsException e) {
            throwOutOfBoundException(address);
        }
    }

    @Override
    public byte loadByte(long address) throws MemoryOutOfBoundsException {
        try {
            return memory[(int) address];
        } catch (ArrayIndexOutOfBoundsException e) {
            throwOutOfBoundException(address);
            return 0;
        }
    }

    @Override
    public short loadHalf(long address) throws MemoryOutOfBoundsException {
        try {
            short result = 0;
            result |= (memory[(int) address] << 8) & 0xff;
            result |= memory[(int) address + 1] & 0xff;
            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            throwOutOfBoundException(address);
            return 0;
        }
    }

    @Override
    public int loadWord(long address) throws MemoryOutOfBoundsException {
        try {
            int result = 0;
            result |= (memory[(int) address] & 0xff) << 24;
            result |= (memory[(int) address + 1] & 0xff) << 16;
            result |= (memory[(int) address + 2] & 0xff) << 8;
            result |= (memory[(int) address + 3] & 0xff);
            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            throwOutOfBoundException(address);
            return 0;
        }
    }

    @Override
    public byte loadByteConst(long address) throws MemoryOutOfBoundsException {
        return loadByte(address);
    }

    protected byte[] getAll() {
        return memory;
    }

    private void throwOutOfBoundException(long address) {
        throw new MemoryOutOfBoundsException("Cannot access address 0x" + Long.toHexString(address)
                + " out of limit: 0x" + Long.toHexString(memory.length));
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
