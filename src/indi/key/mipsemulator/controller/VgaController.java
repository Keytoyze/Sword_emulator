package indi.key.mipsemulator.controller;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.util.TimingRenderer;
import indi.key.mipsemulator.vga.VgaProvider;
import indi.key.mipsemulator.vga.VgaConfigures;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class VgaController implements TickCallback {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private WritableImage content;

    private VgaProvider vgaProvider;

    public VgaController(ImageView screen, Cpu cpu) {
        content = new WritableImage(WIDTH, HEIGHT);

        screen.setImage(content);
        vgaProvider = VgaConfigures.getProvider(cpu);

        TimingRenderer.register(this);
    }

    @Override
    public void onTick() {
        if (VgaConfigures.getResolution() == VgaConfigures.Resolution.CLOSE) {
            return;
        }
        content.getPixelWriter().setPixels(0, 0, WIDTH, HEIGHT, PixelFormat.getByteBgraPreInstance()
                , vgaProvider.getRgbBytes(), 0, WIDTH * 4);
    }
}
