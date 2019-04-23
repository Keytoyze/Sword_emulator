package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;

public class GraphProvider extends VgaProvider {

    @Override
    protected MemoryType getBindMemory() {
        return MemoryType.VRAM_GRAPH;
    }

    public GraphProvider(Cpu cpu) {
        super(cpu);
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
