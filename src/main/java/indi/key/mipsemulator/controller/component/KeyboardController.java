package indi.key.mipsemulator.controller.component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.PS2Key;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.storage.RegisterMemory;
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
        byte[] releaseCode = ps2Key.getReleaseCode();
        for (byte b : releaseCode) {
            ps2Memory.push(b);
        }
    }

    public static class PS2Memory extends RegisterMemory {

        private Queue<Byte> keyQueue = new LinkedBlockingQueue<>();

        void push(Byte b) {
            keyQueue.add(b);
        }

        public PS2Memory(int depth) {
            super();
        }

        @Override
        protected void beforeLoad() {
            Byte key = keyQueue.poll();
            if (key != null) {
                content.setTo(0, 0b10000000, 8);
                content.setTo(24, ((int) key) & 0xff, 8);
                LogUtils.m("read PS2 data: 0x" + content.toHexString() + " (" + content.toString() + ")");
            } else {
                content.set(31, false);
            }
        }
    }
}
