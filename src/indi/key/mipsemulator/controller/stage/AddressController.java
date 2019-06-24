package indi.key.mipsemulator.controller.stage;

import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.FxUtils;
import indi.key.mipsemulator.util.IoUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddressController implements Initializable {

    @FXML
    Button cancelButton;
    @FXML
    Button saveButton;
    @FXML
    GridPane addressGrid;

    private static String[] memoryName = new String[]{
            "RAM", "VRAM", "7-Segment", "GPIO", "Button", "Counter", "PS2 Keyboard"
    };

    private TextField[] texts = new TextField[7];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 7; i++) {
            addressGrid.add(new Label(memoryName[i]), 0, i);
            final TextField textField = new TextField(MemoryType.values()[i].getPref().getAsString());
            texts[i] = textField;
            addressGrid.add(textField, 1, i);
        }
        saveButton.setOnAction(event -> {
            int[] newAddresses = new int[7];
            int i = 0;
            try {
                for (; i < 7; i++) {
                    newAddresses[i] = IoUtils.parseUnsignedInteger(texts[i].getText());
                }
            } catch (Exception e) {
                FxUtils.showException(new IllegalArgumentException("Fail to parse memory: " +
                        memoryName[i] + " (" + e.getMessage() + ")"));
                return;
            }
            for (i = 0; i < 7; i++) {
                MemoryType.values()[i].getPref().set(newAddresses[i]);
            }
            FxUtils.getStage(saveButton).close();
        });
        cancelButton.setOnAction(event -> {
            Stage stage = FxUtils.getStage(cancelButton);
            stage.close();
        });
    }
}
