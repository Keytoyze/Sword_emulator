package indi.key.mipsemulator.util;

import java.util.prefs.Preferences;
import java.util.stream.Stream;

import indi.key.mipsemulator.model.info.BitArray;

public enum SwordPrefs {

    RAM(0x00000000L),
    VRAM(0x000C0000L),
    VRAM_MODE(0xc0000001L),
    SEGMENT(0xFFFFFE00L, 0xE0000000L),
    GPIO(0xFFFFFF00L, 0xF0000000L),
    BUTTON(0xFFFFFC00L, 0xC0000000L),
    COUNTER(0xFFFFFF04L, 0xF0000004L),
    PS2(0xFFFFD000L),

    CLOCK_FREQUENCY(50L),
    DIV(8L),

    DEFAULT_PATH("");


    private static Preferences preferences = null;

    private Long[] values;

    private String stringValue;

    private static Preferences getPrefs() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(SwordPrefs.class);
        }
        return preferences;
    }

    SwordPrefs(Long... defaultValue) {
        //String pref = getPrefs().get(name(), null);
        String pref = null;
        if (pref != null) {
            values = stringToLong(pref);
        } else {
            values = defaultValue;
        }
    }

    SwordPrefs(String defaultValue) {
        stringValue = getPrefs().get(name(), defaultValue);
    }

    public void set(Long... value) {
        this.values = value;
        getPrefs().put(name(), longToString(value));
    }

    public void set(int value) {
        this.set((long) value);
    }

    public Long[] get() {
        return values;
    }

    public String getString() {
        return stringValue;
    }

    public void set(String stringValue) {
        this.stringValue = stringValue;
        getPrefs().put(name(), this.stringValue);
    }

    public String getAsString() {
        return longToString(values);
    }

    public BitArray getAsBits() {
        return BitArray.of(values[0].intValue(), 32);
    }

    private static String longToString(Long... longs) {
        return Stream.of(longs)
                .map(aLong -> "0x" + IoUtils.completeWithZero(Long.toHexString(aLong), 8).toUpperCase())
                .reduce((s, s2) -> s + "; " + s2)
                .orElse("");
    }

    private static Long[] stringToLong(String value) {
        return Stream.of(value.split(";"))
                .map(s -> Integer.toUnsignedLong(IoUtils.parseUnsignedInteger(s.trim())))
                .toArray(Long[]::new);
    }


}
