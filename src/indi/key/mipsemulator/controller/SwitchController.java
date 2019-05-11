package indi.key.mipsemulator.controller;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.BitArray;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SwitchController {

    private GridPane gridPane;
    private GridPane labelPane;
    private CheckBox[] checkBox = new CheckBox[16];
    private Machine machine;
    private BitArray switches;

    public SwitchController(GridPane gridPane, GridPane labelPane, Machine machine) {
        this.gridPane = gridPane;
        this.labelPane = labelPane;
        this.machine = machine;
        this.switches = BitArray.ofLength(16);
        machine.setSwitches(switches);
        initView();
    }

    private void initView() {
        for (int i = 0; i < 16; i++) {
            checkBox[i] = new CheckBox();
            final int index = i;
            checkBox[i].setOnAction(event -> {
                switches.set(index, checkBox[index].isSelected());
                machine.setSwitches(switches);
            });
            gridPane.add(checkBox[i], 15 - i, 0);
            Label label = new Label("" + i);
            labelPane.add(label, 15 - i, 0);
        }
    }
}
