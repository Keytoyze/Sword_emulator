package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.controller.component.VgaController;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.storage.ByteArrayMemory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.IoUtils;

public class TextProvider extends ScreenProvider {

    private byte[] asciiStocks;
    private byte[] gbkStocks;
    private Machine machine;

    public TextProvider(Machine machine) {
        super(machine);
        this.machine = machine;
        asciiStocks = IoUtils.read("/font/ASCII_8.bin");
        gbkStocks = IoUtils.read("/font/HZK_16.bin");
        ((TextVram) machine.getAddressRedirector().getMemory(MemoryType.VRAM_TEXT)).registerProvider(this);
    }

    public static class TextVram extends ByteArrayMemory {

        private VgaConfigures.Font preFont = VgaConfigures.Font.EN_8_8;
        private TextProvider provider = null;
        private CharacterBean[] characterCache = null;

        public TextVram(int depth) {
            super(depth);
        }

        void registerProvider(TextProvider provider) {
            this.provider = provider;
        }

        public void redrawCursor(int preCursorIndex, int afterCursorIndex) {
            long address1 = preCursorIndex * 4;
            long address2 = afterCursorIndex * 4;
            if (address2 > getDepth() || address1 > getDepth() || address1 < 0 || address2 < 0) {
                return;
            }
            onSave(address1, 4);
            onSave(address2, 4);
        }

        @Override
        protected void onSave(long address, int length) {

            int addrInt = (int) address;
            VgaConfigures.Font font = VgaConfigures.getFont();
            if (preFont != font) {
                reset();
                preFont = font;
                onSave(0, MemoryType.VRAM_TEXT.getCapacity());
                return;
            }
            int fontWidth = font.getWidth();
            int fontHeight = font.getHeight();
            if (font.isEN()) {
                for (int i = addrInt - addrInt % 2; i + 4 <= addrInt + length; i += 4) {
                    byte firstByte = loadByte(i + 2);
                    byte secondByte = loadByte(i + 3);
//                    byte[] bytes = memory.load(i + 2, 2);
                    int wordAddr = Byte.toUnsignedInt(secondByte) * 8;
                    BitArray colorBits = BitArray.of(firstByte, 8);
                    int fb = colorBits.get(0) ? 255 : 0;
                    int fg = colorBits.get(1) ? 255 : 0;
                    int fr = colorBits.get(2) ? 255 : 0;
                    int bb = colorBits.get(4) ? 255 : 0;
                    int bg = colorBits.get(5) ? 255 : 0;
                    int br = colorBits.get(6) ? 255 : 0;
                    drawCharacter(fontWidth, fontHeight, i, provider.asciiStocks, wordAddr,
                            fb, fg, fr, bb, bg, br, 8);
                }
            } else {
                for (int i = addrInt - addrInt % 4; i + 4 <= addrInt + length; i += 4) {
//                    byte[] bytes = memory.load(i, 4);

                    byte firstByte = loadByte(i);
                    byte secondByte = loadByte(i + 1);

                    int offset = (94 * (firstByte + 0x60 - 1) + (secondByte + 0x60 - 1)) * 32;
                    int fb = 255;
                    int fg = 255;
                    int fr = 255;
                    int bb = 0;
                    int bg = 0;
                    int br = 0;
                    drawCharacter(fontWidth, fontHeight, i, provider.gbkStocks, offset,
                            fb, fg, fr, bb, bg, br, 16);
                }
            }
        }

        private void drawCharacter(int fontWidth, int fontHeight, int index, byte[] wordStocks,
                                   int address, int fb, int fg, int fr, int bb, int bg, int br, int rawWidth) {
            int charIndex = index / 4;
            if (provider.machine.shouldReverseCursor(charIndex)) {
                fb = ~fb;
                fg = ~fg;
                fr = ~fr;
                bb = ~bb;
                bg = ~bg;
                br = ~br;
            }
            // save cache to avoid redundant drawing
            CharacterBean cache = characterCache[index];
            if (cache != null && cache.address == address && cache.fb == fb && cache.fg == fg
                    && cache.fr == fr && cache.bb == bb && cache.bg == bg && cache.br == br) {
                return;
            }
            characterCache[index] = new CharacterBean(address, fb, fg, fr, bb, bg, br);

            int numberPerLine = VgaController.WIDTH / fontWidth;
            int indexX = charIndex % numberPerLine;
            int indexY = charIndex / numberPerLine;
            int foldX = fontWidth / rawWidth;
            int foldY = fontHeight / rawWidth;
            int marginX = indexX * fontWidth;
            int marginY = indexY * fontHeight;
            byte c;
            for (int offset = 0; offset < rawWidth * rawWidth / 8; offset++) {
                try {
                    c = wordStocks[address + offset];
                } catch (Exception e) {
                    continue;
                }
                int pointIndex = offset * 8;
                for (int d = 0; d < 8; d++) {
                    int pointY = marginY + pointIndex / rawWidth * foldY;
                    int pointX = marginX + pointIndex % rawWidth * foldX;
                    boolean b = (c & 0x80) == 0;
                    for (int m = 0; m < foldX; m++) {
                        for (int n = 0; n < foldY; n++) {
                            if (b) {
                                provider.setRgb(pointY + n, pointX + m, br, bg, bb);
                            } else {
                                provider.setRgb(pointY + n, pointX + m, fr, fg, fb);
                            }
                        }
                    }
                    c <<= 1;
                    pointIndex++;
                }
            }
        }

        @Override
        public void reset() {
            super.reset();
            characterCache = new CharacterBean[getDepth()];
        }
    }

    private static class CharacterBean {
        int address, fb, fg, fr, bb, bg, br;

        CharacterBean(int address, int fb, int fg, int fr, int bb, int bg, int br) {
            this.address = address;
            this.fb = fb;
            this.fg = fg;
            this.fr = fr;
            this.bb = bb;
            this.bg = bg;
            this.br = br;
        }
    }

}
