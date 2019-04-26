package indi.key.mipsemulator.controller;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.util.TimingRenderer;
import indi.key.mipsemulator.vga.ScreenProvider;
import indi.key.mipsemulator.vga.VgaConfigures;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class VgaController implements TickCallback {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private WritableImage content;

    private ScreenProvider screenProvider;

    public VgaController(ImageView screen, Machine machine) {
        content = new WritableImage(WIDTH, HEIGHT);

        screen.setImage(content);
        screenProvider = VgaConfigures.getProvider(machine);

        TimingRenderer.register(this);
    }

    @Override
    public void onTick() {
        if (VgaConfigures.getResolution() == VgaConfigures.Resolution.CLOSE) {
            return;
        }
        content.getPixelWriter().setPixels(0, 0, WIDTH, HEIGHT, PixelFormat.getByteBgraPreInstance()
                , screenProvider.get(), 0, WIDTH * 4);
    }
}
