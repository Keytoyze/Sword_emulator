package indi.key.mipsemulator.core.controller;

import java.util.Arrays;

import indi.key.mipsemulator.model.bean.BitArray;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.TimingRenderer;

public class Counter implements TickCallback {

    private Cpu cpu;
    private boolean ticking = false;
    private long timeStamp;

    private static final byte[] EMPTY = new byte[4];

    public Counter(Cpu cpu) {
        this.cpu = cpu;
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
        Memory memory = cpu.getAddressRedirector().getMemory(MemoryType.GPIO);
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
        Memory counterMem = cpu.getAddressRedirector().getMemory(MemoryType.COUNTER);
        Memory gpioMem = cpu.getAddressRedirector().getMemory(MemoryType.GPIO);
        byte[] bytes = counterMem.load(0, 4);
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - timeStamp;
        long ticks = Integer.toUnsignedLong(IoUtils.bytesToInt(bytes)) - interval * 250000;
        if (ticks < 0) ticks = 0;
        timeStamp = currentTime;
        counterMem.save(0, IoUtils.intToBytes((int) ticks, 32));
        BitArray bitArray = BitArray.ofLength(32).set(31, getCounterOut());
        gpioMem.save(0, bitArray.bytes());
    }
}
