package indi.key.mipsemulator.controller.component;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.controller.TimingRenderer;
import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.vga.GraphProvider;
import indi.key.mipsemulator.vga.ScreenProvider;
import indi.key.mipsemulator.vga.TextProvider;
import indi.key.mipsemulator.vga.VgaConfigures;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class VgaController implements TickCallback, Resetable {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private WritableImage content;
    private Image placeHolder;
    private ImageView screen;
    private Machine machine;

    private GraphProvider graphProvider;
    private TextProvider textProvider;

    public VgaController(ImageView screen, Machine machine) {
        content = new WritableImage(WIDTH, HEIGHT);
        this.machine = machine;
        placeHolder = new Image(VgaController.class.getResourceAsStream("/drawable/placeholder.jpg"));
        this.screen = screen;
        screen.setImage(placeHolder);

        graphProvider = new GraphProvider(machine);
        textProvider = new TextProvider(machine);

        TimingRenderer.register(this);
    }

    @Override
    public void onTick(long ticks) {
        Platform.runLater(() -> {
            if (VgaConfigures.getResolution() == VgaConfigures.Resolution.CLOSE) {
                return;
            }
            if (machine.isLooping() && screen.getImage() != content) {
                screen.setImage(content);
            }
            ScreenProvider currentProvider = VgaConfigures.isTextMode() ? textProvider : graphProvider;
            content.getPixelWriter().setPixels(0, 0, WIDTH, HEIGHT, PixelFormat.getByteBgraPreInstance()
                    , currentProvider.get(), 0, WIDTH * 4);
            // blink cursor
            if (ticks % 10 == 0) {
                machine.blink();
            }
        });

    }

    @Override
    public void reset() {
        graphProvider.reset();
        textProvider.reset();
        screen.setImage(placeHolder);
    }
}
