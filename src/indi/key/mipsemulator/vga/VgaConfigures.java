package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.core.controller.Machine;

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
    }

    public static ScreenProvider getProvider(Machine machine) {
        return new TextProvider(machine);
    }

    public static Font getFont() {
        return Font.EN_8_8;
    }

    public static Resolution getResolution() {
        return Resolution.RE_640_480;
    }

    public static int getAddressOffset() {
        return 0x000C0000;
    }
}
