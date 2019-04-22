package indi.key.mipsemulator.controller;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.util.TimingRenderer;
import indi.key.mipsemulator.vga.VgaGraphProvider;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class VgaController implements TickCallback {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private ImageView screen;
    private WritableImage content;
    private Cpu cpu;

    private VgaGraphProvider graphProvider;

    public VgaController(ImageView screen, Cpu cpu) {
        this.screen = screen;
        content = new WritableImage(WIDTH, HEIGHT);

        screen.setImage(content);
        graphProvider = new VgaGraphProvider(cpu);

        TimingRenderer.register(this);
    }

    @Override
    public void onTick() {

        content.getPixelWriter().setPixels(0, 0, WIDTH, HEIGHT, PixelFormat.getByteBgraPreInstance()
                , graphProvider.getRgbArray(), 0, WIDTH * 4);
    }
}
