package indi.key.mipsemulator.controller.component;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.storage.RegisterMemory;
import indi.key.mipsemulator.util.LogUtils;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ButtonController {

    private GridPane pane;
    private Memory btnMemory;
    private Button[] buttons = new Button[25];
    private Machine machine;
    private static final int BUTTON_WIDTH = 40;

    public ButtonController(GridPane buttonPane, Machine machine) {
        this.pane = buttonPane;
        this.machine = machine;
        this.btnMemory = machine.getAddressRedirector().getMemory(MemoryType.BUTTON);
        machine.setButtons(BitArray.ofLength(5));
        initView();
    }

    private void initView() {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                final BitArray kCodeBit = getKCode(x, y);
                final BitArray btnCode = BitArray.ofLength(5).set(x);
                int kCode = kCodeBit.value();
                buttons[kCode] = new Button(Integer.toHexString(kCode).toUpperCase());
                buttons[kCode].setPrefWidth(BUTTON_WIDTH);
                buttons[kCode].setPrefHeight(BUTTON_WIDTH);
                buttons[kCode].setStyle(" -fx-border-radius: 50; -fx-background-radius:50; -fx-font-size: 15;");
                buttons[kCode].setOnAction(event -> {
                    machine.setButtons(btnCode);
                    BitArray data = BitArray.of(
                            BitArray.of(1, 1), // 1
                            BitArray.ofLength(21), // 21'h000000
                            btnCode, kCodeBit);
                    LogUtils.m("save button data: " + data.toHexString() + " (" + data.toString() + ")");
                    btnMemory.save(0, data.bytes());
                });
                pane.add(buttons[kCode], x, 4 - y);
            }
        }
    }

    /* SWROD-V4
        10 11 12 13 14
        0  1  2  3  15
        4  5  6  7  16
        8  9  A  B  17
        C  D  E  F  18
    */
    private static BitArray getKCode(int x, int y) {
        if (x <= 3 && y <= 3) {
            return BitArray.ofValue(x + (3 - y) * 4).setLength(5);
        } else if (y == 4) {
            return BitArray.ofValue(16 + x).setLength(5);
        } else {
            return BitArray.ofValue(24 - y).setLength(5);
        }
    }

    public static class ButtonMemory extends RegisterMemory {

        public ButtonMemory(int depth) {
            super();
        }

        @Override
        public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
            byte[] bytes = super.load(address, bytesNum);
            getBitArray().set(31, false);
            return bytes;
        }
    }
}
