package indi.key.mipsemulator.vga;

import java.util.function.Supplier;

import indi.key.mipsemulator.controller.VgaController;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.interfaces.MemoryListener;
import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.storage.MemoryType;

public abstract class ScreenProvider implements MemoryListener, Resetable, Supplier<byte[]> {

    /* Pixels in this format can be decoded using the following sample code:
     *     int i = rowstart + x * 4;
     *
     *     int blue  = (array[i+0] & 0xff);
     *     int green = (array[i+1] & 0xff);
     *     int red   = (array[i+2] & 0xff);
     *     int alpha = (array[i+3] & 0xff);
     */
    byte[] rgbBytes = new byte[VgaController.HEIGHT * VgaController.WIDTH * 4];

    protected abstract MemoryType getBindMemory();

    public ScreenProvider(Machine machine) {
        machine.getAddressRedirector().addListener(getBindMemory(), this);
        reset();
    }

    @Override
    public byte[] get() {
        return rgbBytes;
    }

    void setRgb(int y, int x, int r, int g, int b) {
        int index = (y * VgaController.WIDTH + x) * 4;
        rgbBytes[index] = (byte) b;
        rgbBytes[index + 1] = (byte) g;
        rgbBytes[index + 2] = (byte) r;
    }

    @Override
    public void reset() {
        for (int i = 0; i < rgbBytes.length; i++) {
            rgbBytes[i] = i % 4 == 3 ? (byte) 255 : 0;
        }
    }
}
