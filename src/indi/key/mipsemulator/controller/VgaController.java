package indi.key.mipsemulator.controller;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.core.controller.TimingRenderer;
import indi.key.mipsemulator.vga.GraphProvider;
import indi.key.mipsemulator.vga.ScreenProvider;
import indi.key.mipsemulator.vga.TextProvider;
import indi.key.mipsemulator.vga.VgaConfigures;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class VgaController implements TickCallback {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private WritableImage content;

    private GraphProvider graphProvider;
    private TextProvider textProvider;

    public VgaController(ImageView screen, Machine machine) {
        content = new WritableImage(WIDTH, HEIGHT);
        screen.setImage(content);

        graphProvider = new GraphProvider(machine);
        textProvider = new TextProvider(machine);

        TimingRenderer.register(this);
    }

    @Override
    public void onTick() {
        if (VgaConfigures.getResolution() == VgaConfigures.Resolution.CLOSE) {
            return;
        }
        ScreenProvider currentProvider = VgaConfigures.isTextMode() ? textProvider : graphProvider;
        content.getPixelWriter().setPixels(0, 0, WIDTH, HEIGHT, PixelFormat.getByteBgraPreInstance()
                , currentProvider.get(), 0, WIDTH * 4);
    }
}
