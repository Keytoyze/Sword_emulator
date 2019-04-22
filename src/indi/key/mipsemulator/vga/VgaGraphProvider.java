package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.controller.VgaController;
import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.model.interfaces.MemoryListener;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;

public class VgaGraphProvider implements VgaProvider, MemoryListener {

    /* Pixels in this format can be decoded using the following sample code:
     *     int i = rowstart + x * 4;
     *
     *     int blue  = (array[i+0] & 0xff);
     *     int green = (array[i+1] & 0xff);
     *     int red   = (array[i+2] & 0xff);
     *     int alpha = (array[i+3] & 0xff);
     */
    private byte[] rgbBytes = new byte[VgaController.HEIGHT * VgaController.WIDTH * 4];

    public VgaGraphProvider(Cpu cpu) {
        cpu.getAddressRedirector().setListener(MemoryType.VRAM_GRAPH, this);
        for (int i = 3; i < rgbBytes.length; i += 4) {
            rgbBytes[i] = (byte) 255;
        }
    }

    @Override
    public byte[] getRgbArray() {
        return rgbBytes;
    }

    /*
     * xxxx_RRRR_GGGG_BBBB
     */
    @Override
    public void onMemoryChange(Memory memory, int address, int length) {
        int initAddr = address - address % 2;
        for (int i = initAddr; i < address + length; i += 2) {
            byte[] bytes = memory.load(i, 2);
            int index = i / 2;
            byte b = (byte) (bytes[1] & 0xf);
            byte g = (byte) (bytes[1] >>> 4);
            byte r = (byte) (bytes[0] & 0xf);
            rgbBytes[index] = (byte) (b << 4 | b);
            rgbBytes[index + 1] = (byte) (g << 4 | g);
            rgbBytes[index + 2] = (byte) (r << 4 | r);
        }
    }
}
