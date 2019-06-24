package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.util.SwordPrefs;

public class VgaConfigures {

    public enum Font {
        EN_8_8(0b000, 8, 8, true),
        EN_8_16(0b001, 8, 16, true),
        ZH_16_16(0b010, 16, 16, false),
        ZH_32_32(0b011, 32, 32, false);

        private byte b;
        private int height, width;
        private boolean en;

        Font(int b, int width, int height, boolean isEN) {
            this.b = (byte) b;
            this.height = height;
            this.width = width;
            this.en = isEN;
        }

        public static Font parse(BitArray code) {
            for (Font font : values()) {
                if (font.b == code.value()) {
                    return font;
                }
            }
            throw new IllegalArgumentException("Cannot parse font code: " +
                    code);
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public boolean isEN() {
            return en;
        }
    }

    public enum Resolution {

        CLOSE(0b0000),
        RE_640_480(0b0001);

        private byte b;

        Resolution(int b) {
            this.b = (byte) b;
        }

        public static Resolution parse(BitArray code) {
            for (Resolution resolution : values()) {
                if (resolution.b == code.value()) {
                    return resolution;
                }
            }
            throw new IllegalArgumentException("Cannot parse resolotion code: " +
                    code);
        }
    }

    private static boolean mTextMode = false;
    private static Font mFont = Font.EN_8_8;
    private static Resolution mResolution = Resolution.RE_640_480;
    private static int mAddressOffset = 0x000C0000;
    private static Runnable onFontChangedCallback = null;

    static {
        setModeRegister(SwordPrefs.VRAM_MODE.getAsBits());
    }

    private static void parseModeBits(BitArray bitArray) {
        mTextMode = !bitArray.get(31);
        mFont = Font.parse(bitArray.subArray(4, 7));
        mResolution = Resolution.parse(bitArray.subArray(0, 4));
    }

    public static void setModeRegister(BitArray bitArray) {
        SwordPrefs.VRAM_MODE.set(Long.valueOf(bitArray.value()));
        parseModeBits(bitArray);
    }

    public static BitArray getModeRegister() {
        return SwordPrefs.VRAM_MODE.getAsBits();
    }

    public static boolean isTextMode() {
        return mTextMode;
    }

    public static void setTextMode(boolean textMode) {
        mTextMode = textMode;
        if (onFontChangedCallback != null) {
            onFontChangedCallback.run();
        }
    }

    public static Font getFont() {
        return mFont;
    }

    public static void setFont(Font font) {
        mFont = font;
        if (mTextMode && onFontChangedCallback != null) {
            onFontChangedCallback.run();
        }
    }

    public static void setOnFontChangedCallback(Runnable callback) {
        onFontChangedCallback = callback;
    }

    public static Resolution getResolution() {
        return mResolution;
    }

    public static void setAddressOffsetRegister(BitArray bitArray) {
        mAddressOffset = bitArray.value();
    }

    public static BitArray getAddressOffsetRegister() {
        return BitArray.of(mAddressOffset, 32);
    }

    public static int getAddressOffset() {
        return mAddressOffset;
    }
}
