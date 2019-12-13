package indi.key.mipsemulator.core.controller;

import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.SwordPrefs;

public class Counter implements TickCallback {

    private Memory counterMemory;
    private boolean ticking = false;
    private long timeStamp;

    private static final byte[] EMPTY = new byte[4];

    public Counter(Machine machine) {
        counterMemory = machine.getAddressRedirector().getMemory(MemoryType.COUNTER);
    }

    public void ticks() {
        onTick(0);
    }

    public void beginTicking() {
        if (!ticking) {
            ticking = true;
            timeStamp = System.currentTimeMillis();
            TimingRenderer.register(this);
        }
    }

    public boolean getCounterOut() {
        return counterMemory.loadWord(0) == 0;
    }

    public void endTicking() {
        if (ticking) {
            ticking = false;
            TimingRenderer.unRegister(this);
        }
    }

    @Override
    public void onTick(long t) {
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - timeStamp;
        double ticks = Integer.toUnsignedLong(counterMemory.loadWord(0)) - interval / Math.pow(2,
                Integer.parseInt(SwordPrefs.DIV.get())) *
                Integer.parseInt(SwordPrefs.CLOCK_FREQUENCY.get()) * 1000;
        //LogUtils.i(Integer.toUnsignedLong(IoUtils.bytesToInt(IoUtils.intToBytes((int) ticks, 32))), Integer.toUnsignedLong((int) ticks));
        if (ticks < 0) ticks = 0;
        timeStamp = currentTime;
        counterMemory.saveWord(0, (int) ticks);
    }
}
