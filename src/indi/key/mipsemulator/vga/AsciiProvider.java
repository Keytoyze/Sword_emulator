package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.controller.VgaController;
import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.IoUtils;

public class AsciiProvider extends VgaProvider {

    private byte[] asciiStocks;

    public AsciiProvider(Cpu cpu) {
        super(cpu);
        asciiStocks = IoUtils.read("/res/font/ASCII_8.bin");
    }

    @Override
    protected MemoryType getBindMemory() {
        return MemoryType.VRAM_TEXT;
    }

    @Override
    public void onMemoryChange(Memory memory, int address, int length) {
        for (int i = address - address % 2; i < address + length; i += 2) {
            int width = VgaConfigures.getFont().getWidth();
            int height = VgaConfigures.getFont().getHeight();
            int wn = VgaController.WIDTH / width;
            int hn = VgaController.HEIGHT / height;
            int nx = i / 2 % wn;
            int ny = i / 2 / wn;
            int offsetX = nx * width;
            int offsetY = ny * height;
            byte c;

            byte[] bytes = memory.load(i, 2);

            byte chara = bytes[0];

            int a = chara * 8;
            for (int k = 0; k < 8; k++) {
                c = asciiStocks[a + k];
                for (int d = 0; d < 8; d++) {
                    if ((c & 0x80) == 0) {
                        setRgb(offsetY + k, offsetX + d, 0, 0, 0);
                    } else {
                        setRgb(offsetY + k, offsetX + d, 255, 255, 255);
                    }
                    c <<= 1;
                }
            }
        }
    }
}
