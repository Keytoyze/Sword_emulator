package indi.key.mipsemulator.controller;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.util.TimingRenderer;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LedController implements TickCallback {

    private Machine machine;
    private Circle[] circles = new Circle[16];

    public LedController(GridPane ledPane, Machine machine) {
        this.machine = machine;
        for (int i = 0; i < 16; i++) {
            circles[i] = new Circle(3);
            circles[i].setFill(Color.GRAY);
            ledPane.add(circles[i], i, 0);
        }
        TimingRenderer.register(this);
    }

    @Override
    public void onTick() {
        BitArray led = machine.getLed();
        assert led.length() == 16;
        int leds = led.value();
        int k = 1;
        for (int i = 0; i < 16; i++) {
            if ((leds & k) != 0) {
                circles[15 - i].setFill(Color.YELLOW);
            } else {
                circles[15 - i].setFill(Color.GRAY);
            }
            k <<= 1;
        }
    }
}
