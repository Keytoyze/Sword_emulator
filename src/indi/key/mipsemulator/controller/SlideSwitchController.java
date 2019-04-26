package indi.key.mipsemulator.controller;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SlideSwitchController {

    private GridPane gridPane;
    private GridPane labelPane;
    private CheckBox[] checkBox = new CheckBox[16];

    public SlideSwitchController(GridPane gridPane, GridPane labelPane) {
        this.gridPane = gridPane;
        this.labelPane = labelPane;
        //cpu.addRegisterListener(this);
        initView();
    }

    private void initView() {
        for (int i = 0; i < 16; i++) {
            checkBox[i] = new CheckBox();
            gridPane.add(checkBox[i], 15 - i, 0);
            Label label = new Label("" + i);
            labelPane.add(label, 15 - i, 0);
        }
    }
}
