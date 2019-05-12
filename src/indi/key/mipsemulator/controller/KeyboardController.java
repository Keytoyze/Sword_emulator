package indi.key.mipsemulator.controller;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.info.PS2Key;
import indi.key.mipsemulator.storage.ByteArrayMemory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.LogUtils;
import javafx.scene.input.KeyCode;

public class KeyboardController {


    private PS2Memory ps2Memory;

    public KeyboardController(Machine machine) {
        ps2Memory = (PS2Memory) machine.getAddressRedirector().getMemory(MemoryType.PS2);
    }

    public void press(KeyCode keyCode) {
        PS2Key ps2Key = PS2Key.of(keyCode);
        byte[] pressCode = ps2Key.getPressCode();
        for (byte b : pressCode) {
            ps2Memory.push(b);
        }
    }

    public void release(KeyCode keyCode) {
        PS2Key ps2Key = PS2Key.of(keyCode);
        LogUtils.i(ps2Key);
        byte[] releaseCode = ps2Key.getReleaseCode();
        for (byte b : releaseCode) {
            ps2Memory.push(b);
        }
    }

    public static class PS2Memory extends ByteArrayMemory {

        private Queue<Byte> keyQueue = new LinkedBlockingQueue<>();

        void push(Byte b) {
            keyQueue.add(b);
        }

        public PS2Memory(int depth) {
            super(depth);
        }

        @Override
        public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
            Byte key = keyQueue.poll();
            byte[] bytes = super.load(0, 4);
            if (key != null) {
                bytes[3] = 0b1000;
                bytes[0] = key;
                LogUtils.m("Send to PS2 memory: " + BitArray.of(bytes).toHexString());
            } else {
                bytes = BitArray.of(bytes).set(31, false).bytes();
            }
            super.save(0, bytes);
            return super.load(address, bytesNum);
        }
    }
}
