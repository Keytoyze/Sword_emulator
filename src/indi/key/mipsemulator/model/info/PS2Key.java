package indi.key.mipsemulator.model.info;

import javafx.scene.input.KeyCode;

@SuppressWarnings("unused")
public enum PS2Key {

    A(KeyCode.A, 0x1C),
    B(KeyCode.B, 0x32),
    C(KeyCode.C, 0x21),
    D(KeyCode.D, 0x23),
    E(KeyCode.E, 0x24),
    F(KeyCode.F, 0x2B),
    G(KeyCode.G, 0x34),
    H(KeyCode.H, 0x33),
    I(KeyCode.I, 0x43),
    J(KeyCode.J, 0x3B),
    K(KeyCode.K, 0x42),
    L(KeyCode.L, 0x4B),
    M(KeyCode.M, 0x3A),
    N(KeyCode.N, 0x31),
    O(KeyCode.O, 0x44),
    P(KeyCode.P, 0x4D),
    Q(KeyCode.Q, 0x15),
    R(KeyCode.R, 0x2D),
    S(KeyCode.S, 0x1B),
    T(KeyCode.T, 0x2C),
    U(KeyCode.U, 0x3C),
    V(KeyCode.V, 0x2A),
    W(KeyCode.W, 0x1D),
    X(KeyCode.X, 0x22),
    Y(KeyCode.Y, 0x35),
    Z(KeyCode.Z, 0x1A),
    DIGIT0(KeyCode.DIGIT0, 0x45),
    DIGIT1(KeyCode.DIGIT1, 0x16),
    DIGIT2(KeyCode.DIGIT2, 0x1E),
    DIGIT3(KeyCode.DIGIT3, 0x26),
    DIGIT4(KeyCode.DIGIT4, 0x25),
    DIGIT5(KeyCode.DIGIT5, 0x2E),
    DIGIT6(KeyCode.DIGIT6, 0x36),
    DIGIT7(KeyCode.DIGIT7, 0x3D),
    DIGIT8(KeyCode.DIGIT8, 0x3E),
    DIGIT9(KeyCode.DIGIT9, 0x46),
    BACK_QUOTE(KeyCode.BACK_QUOTE, 0x0E),
    MINUS(KeyCode.MINUS, 0x4E),
    EQUALS(KeyCode.EQUALS, 0x55),
    BACK_SLASH(KeyCode.BACK_SLASH, 0x5D),
    BACK_SPACE(KeyCode.BACK_SPACE, 0x66),
    SPACE(KeyCode.SPACE, 0x29),
    TAB(KeyCode.TAB, 0x0D),
    CAPS(KeyCode.CAPS, 0x58),
    SHIFT(KeyCode.SHIFT, 0x12),
    CONTROL(KeyCode.CONTROL, 0x14),
    ALT(KeyCode.ALT, 0x11),
    //    L_SHFT(KeyCode.L_SHFT, 0x12),
    //    L_CTRL(KeyCode.L_CTRL, 0x14),
    //    L_GUI(KeyCode.L_GUI, 0x1F, true),
    //    L_ALT(KeyCode.L_ALT, 0x11),
    //    R_SHFT(KeyCode.R_SHFT, 0x59),
    //    R_CTRL(KeyCode.R_CTRL, 0x14, true),
    //    R_GUI(KeyCode.R_GUI, 0x27, true),
    //    R_ALT(KeyCode.R_ALT, 0x11, true),
    //    APPS(KeyCode.APPS, 0x2F, true),
    ENTER(KeyCode.ENTER, 0x5A),
    ESCAPE(KeyCode.ESCAPE, 0x76),
    F1(KeyCode.F1, 0x5),
    F2(KeyCode.F2, 0x6),
    F3(KeyCode.F3, 0x4),
    F4(KeyCode.F4, 0x0C),
    F5(KeyCode.F5, 0x3),
    F6(KeyCode.F6, 0x0B),
    F7(KeyCode.F7, 0x83),
    F8(KeyCode.F8, 0x0A),
    F9(KeyCode.F9, 0x1),
    F10(KeyCode.F10, 0x9),
    F11(KeyCode.F11, 0x78),
    F12(KeyCode.F12, 0x7),
    PRINTSCREEN(KeyCode.PRINTSCREEN, new byte[]{Constants.E0, (byte) 0x12, Constants.E0, (byte) 0x7C},
            new byte[]{Constants.E0, Constants.F0, (byte) 0x7C, Constants.E0, Constants.F0, (byte) 0x12}),
    SCROLL_LOCK(KeyCode.SCROLL_LOCK, 0x7E),
    PAUSE(KeyCode.PAUSE, new byte[]{Constants.E1, 0x14, 0x77, Constants.E1, Constants.F0, 0x14, Constants.F0, 77},
            new byte[]{}),
    OPEN_BRACKET(KeyCode.OPEN_BRACKET, 0x54),
    INSERT(KeyCode.INSERT, 0x70, true),
    HOME(KeyCode.HOME, 0x6C, true),
    PAGE_UP(KeyCode.PAGE_UP, 0x7D, true),
    DELETE(KeyCode.DELETE, 0x71, true),
    END(KeyCode.END, 0x69, true),
    PAGE_DOWN(KeyCode.PAGE_DOWN, 0x7A, true),
    UP(KeyCode.UP, 0x75, true),
    LEFT(KeyCode.LEFT, 0x6B, true),
    DOWN(KeyCode.DOWN, 0x72, true),
    RIGHT(KeyCode.RIGHT, 0x74, true),
    NUM_LOCK(KeyCode.NUM_LOCK, 0x77),
    DIVIDE(KeyCode.DIVIDE, 0x4A, true),
    MULTIPLY(KeyCode.MULTIPLY, 0x7C),
    SUBTRACT(KeyCode.SUBTRACT, 0x7B),
    ADD(KeyCode.ADD, 0x79),
    //    KP
    //    EN(KeyCode.KP_EN, 0xE0 5 A),
    DECIMAL(KeyCode.DECIMAL, 0x71),
    NUMPAD0(KeyCode.NUMPAD0, 0x70),
    NUMPAD1(KeyCode.NUMPAD1, 0x69),
    NUMPAD2(KeyCode.NUMPAD2, 0x72),
    NUMPAD3(KeyCode.NUMPAD3, 0x7A),
    NUMPAD4(KeyCode.NUMPAD4, 0x6B),
    NUMPAD5(KeyCode.NUMPAD5, 0x73),
    CLEAR(KeyCode.CLEAR, 0x73),
    NUMPAD6(KeyCode.NUMPAD6, 0x74),
    NUMPAD7(KeyCode.NUMPAD7, 0x6C),
    NUMPAD8(KeyCode.NUMPAD8, 0x75),
    NUMPAD9(KeyCode.NUMPAD9, 0x7D),
    CLOSE_BRACKET(KeyCode.CLOSE_BRACKET, 0x58),
    SEMICOLON(KeyCode.SEMICOLON, 0x4C),
    QUOTE(KeyCode.QUOTE, 0x52),
    COMMA(KeyCode.COMMA, 0x41),
    PERIOD(KeyCode.PERIOD, 0x49),
    SLASH(KeyCode.SLASH, 0x4A),
    UNDIFINE(KeyCode.UNDEFINED, new byte[]{}, new byte[]{});

    private interface Constants {
        byte E0 = (byte) 0xE0;
        byte E1 = (byte) 0xE1;
        byte F0 = (byte) 0xF0;
    }

    private KeyCode keyCode;
    private byte[] pressCode;
    private byte[] releaseCode;

    PS2Key(KeyCode keycode, int scanCode) {
        this(keycode, scanCode, false);
    }

    PS2Key(KeyCode keyCode, int scanCode, boolean special) {
        this(keyCode, getPressCode(scanCode, special), getReleaseCode(scanCode, special));
    }

    PS2Key(KeyCode keyCode, byte[] pressCode, byte[] releaseCode) {
        this.keyCode = keyCode;
        this.pressCode = pressCode;
        this.releaseCode = releaseCode;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public byte[] getPressCode() {
        return pressCode;
    }

    public byte[] getReleaseCode() {
        return releaseCode;
    }

    public static PS2Key of(KeyCode keyCode) {
        for (PS2Key ps2Key : values()) {
            if (ps2Key.keyCode == keyCode) {
                return ps2Key;
            }
        }
        return UNDIFINE;
    }

    private static byte[] getPressCode(int scanCode, boolean special) {
        if (special) {
            return new byte[]{Constants.E0, (byte) scanCode};
        } else {
            return new byte[]{(byte) scanCode};
        }
    }

    private static byte[] getReleaseCode(int scanCode, boolean special) {
        if (special) {
            return new byte[]{Constants.E0, Constants.F0, (byte) scanCode};
        } else {
            return new byte[]{Constants.F0, (byte) scanCode};
        }
    }
}
