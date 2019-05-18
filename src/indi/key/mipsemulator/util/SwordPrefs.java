package indi.key.mipsemulator.util;

import java.util.prefs.Preferences;

// TODO: save devices address to this.
public enum SwordPrefs {

    ;

    private static Preferences preferences = null;

    private static Preferences getPrefs() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(SwordPrefs.class);
        }
        return preferences;
    }

    private SwordPrefs() {
    }


}
