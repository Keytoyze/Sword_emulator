package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.storage.ByteArrayMemory;
import indi.key.mipsemulator.storage.MemoryType;

public class GraphProvider extends ScreenProvider {

    public GraphProvider(Machine machine) {
        super(machine);
        ((GraphVram) machine.getAddressRedirector().getMemory(MemoryType.VRAM_GRAPH)).registerProvider(this);
    }

    public static class GraphVram extends ByteArrayMemory {

        public GraphVram(int depth) {
            super(depth);
        }

        private ScreenProvider provider = null;

        void registerProvider(ScreenProvider provider) {
            this.provider = provider;
        }

        /*
         * xxxx_RRRR_GGGG_BBBB
         */
        @Override
        protected void onSave(long address, int length) {
            if (provider != null) {
                int initAddr = (int) (address - address % 2);
                for (int i = initAddr; i < address + length; i += 2) {
                    byte firstByte = loadByte(address);
                    byte secondByte = loadByte(address + 1);
                    int index = i * 2;
                    byte b = (byte) (secondByte & 0xf);
                    byte g = (byte) ((secondByte & 0xf0) >>> 4);
                    byte r = (byte) (firstByte & 0xf);
                    provider.rgbBytes[index] = (byte) (b << 4 | b);
                    provider.rgbBytes[index + 1] = (byte) (g << 4 | g);
                    provider.rgbBytes[index + 2] = (byte) (r << 4 | r);
                }
            }
        }
    }
}
