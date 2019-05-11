package indi.key.mipsemulator.core.controller;

import java.util.Arrays;

import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.IoUtils;

public class Counter implements TickCallback {

    private Machine machine;
    private boolean ticking = false;
    private long timeStamp;

    private static final byte[] EMPTY = new byte[4];

    public Counter(Machine machine) {
        this.machine = machine;
    }

    public void ticks() {
        onTick();
    }

    public void beginTicking() {
        if (!ticking) {
            ticking = true;
            timeStamp = System.currentTimeMillis();
            TimingRenderer.register(this);
        }
    }

    public boolean getCounterOut() {
        Memory memory = machine.getAddressRedirector().getMemory(MemoryType.COUNTER);
        byte[] bytes = memory.load(0, 4);
        return Arrays.equals(bytes, EMPTY);
    }

    public void endTicking() {
        if (ticking) {
            ticking = false;
            TimingRenderer.unRegister(this);
        }
    }

    @Override
    public void onTick() {
        Memory counterMem = machine.getAddressRedirector().getMemory(MemoryType.COUNTER);
        byte[] bytes = counterMem.load(0, 4);
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - timeStamp;
        long ticks = Integer.toUnsignedLong(IoUtils.bytesToInt(bytes)) - interval * 256;
        //LogUtils.i(Integer.toUnsignedLong(IoUtils.bytesToInt(IoUtils.intToBytes((int) ticks, 32))), Integer.toUnsignedLong((int) ticks));
        if (ticks < 0) ticks = 0;
        timeStamp = currentTime;
        counterMem.save(0, IoUtils.intToBytes((int) ticks, 32));
    }
}
