package indi.key.mipsemulator.controller;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.info.PS2Key;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.ByteArrayMemory;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.LogUtils;
import javafx.scene.input.KeyCode;

public class KeyboardController implements TickCallback {

    private Queue<Byte> keyQueue = new LinkedBlockingQueue<>();
    private Memory ps2Memory;

    public KeyboardController(Machine machine) {
        machine.addCpuListener(this);
        ps2Memory = machine.getAddressRedirector().getMemory(MemoryType.PS2);
    }

    public void press(KeyCode keyCode) {
        PS2Key ps2Key = PS2Key.of(keyCode);
        LogUtils.i(ps2Key);
        byte[] pressCode = ps2Key.getPressCode();
        for (byte b : pressCode) {
            keyQueue.add(b);
        }
    }

    public void release(KeyCode keyCode) {
        PS2Key ps2Key = PS2Key.of(keyCode);
        LogUtils.i(ps2Key);
        byte[] releaseCode = ps2Key.getReleaseCode();
        for (byte b : releaseCode) {
            keyQueue.add(b);
        }
    }

    @Override
    public void onTick() {
        byte[] bytes = ps2Memory.load(0, 4);
        if ((bytes[3] & 0x80) == 0) {
            Byte key = keyQueue.poll();
            if (key != null) {
                bytes[3] = 0b1000;
                bytes[0] = key;
                LogUtils.i(Integer.toHexString(Byte.toUnsignedInt(key)));
                ps2Memory.save(0, bytes);
            }
        }
    }

    public static class PS2Memory extends ByteArrayMemory {

        public PS2Memory(int depth) {
            super(depth);
        }

        @Override
        public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
            byte[] bytes = super.load(address, bytesNum);
            save(address, BitArray.of(bytes).set(31, false).bytes());
            return bytes;
        }
    }
}
