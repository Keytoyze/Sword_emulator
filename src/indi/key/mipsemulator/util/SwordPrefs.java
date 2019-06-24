package indi.key.mipsemulator.util;

import java.util.prefs.Preferences;

public enum SwordPrefs {

    RAM("0x00000000"),
    VRAM("0x000C0000"),
    VRAM_MODE("0xc0000001"),
    SEGMENT("0xFFFFFE00; 0xE0000000"),
    GPIO("0xFFFFFF00; 0xF0000000"),
    BUTTON("0xFFFFFC00; 0xC0000000"),
    COUNTER("0xFFFFFF04; 0xF0000004"),
    PS2("0xFFFFD000"),

    CLOCK_FREQUENCY("50"),
    DIV("8"),

    DEFAULT_PATH("");


    private static Preferences preferences = null;

    private String value;

    private static Preferences getPrefs() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(SwordPrefs.class);
        }
        return preferences;
    }

    SwordPrefs(String defaultValue) {
        value = getPrefs().get(name(), defaultValue);
    }

    public String get() {
        return value;
    }

    public void set(String stringValue) {
        this.value = stringValue;
        getPrefs().put(name(), this.value);
    }


}
