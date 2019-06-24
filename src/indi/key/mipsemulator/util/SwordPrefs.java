package indi.key.mipsemulator.util;

import java.util.prefs.Preferences;

import indi.key.mipsemulator.model.info.BitArray;

public enum SwordPrefs {

    RAM(0x00000000L),
    VRAM(0x000C0000L),
    VRAM_MODE(0xc0000001L),
    SEGMENT(0xFFFFFE00L),
    GPIO(0xFFFFFF00L),
    BUTTON(0xFFFFFC00L),
    COUNTER(0xFFFFFF04L),
    PS2(0xFFFFD000L),

    CLOCK_FREQUENCY(50),
    DIV(8);


    private static Preferences preferences = null;

    private long value;

    private static Preferences getPrefs() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(SwordPrefs.class);
        }
        return preferences;
    }

    SwordPrefs(long defaultValue) {
        value = getPrefs().getLong(name(), defaultValue);
    }

    public void set(int value) {
        this.value = Integer.toUnsignedLong(value);
        getPrefs().putLong(name(), this.value);
    }

    public long get() {
        return value;
    }

    public String getAsString() {
        return "0x" + IoUtils.completeWithZero(Integer.toHexString(BitArray.of((int) value, 32).value())
                .toUpperCase(), 8);
    }

    public BitArray getAsBits() {
        return BitArray.of((int) value, 32);
    }


}
