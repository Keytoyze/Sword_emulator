package indi.key.mipsemulator.controller.stage;

import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.util.FxUtils;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.SwordPrefs;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ClockController implements Initializable {

    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;
    @FXML
    TextField clockFrequencyText;
    @FXML
    TextField divText;
    @FXML
    Label countFrequencyLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        divText.setText(SwordPrefs.DIV.get());
        clockFrequencyText.setText(SwordPrefs.CLOCK_FREQUENCY.get());
        divText.textProperty().addListener((observable, oldValue, newValue) -> calculateFrequency());
        clockFrequencyText.textProperty().addListener((observable, oldValue, newValue) -> calculateFrequency());
        calculateFrequency();
        saveButton.setOnAction(event -> {
            int div, clock;
            try {
                div = IoUtils.parseUnsignedInteger(divText.getText());
                clock = IoUtils.parseUnsignedInteger(clockFrequencyText.getText());
            } catch (Exception e) {
                FxUtils.showException(e);
                return;
            }
            SwordPrefs.DIV.set("" + div);
            SwordPrefs.CLOCK_FREQUENCY.set("" + clock);
            FxUtils.getStage(countFrequencyLabel).close();
        });
        cancelButton.setOnAction(event -> FxUtils.getStage(countFrequencyLabel).close());
    }

    private void calculateFrequency() {
        try {
            long div = IoUtils.parseUnsignedInteger(divText.getText());
            long clock = IoUtils.parseUnsignedInteger(clockFrequencyText.getText());
            countFrequencyLabel.setText(String.format("%.7f",  + Math.pow(2, div) / clock / 1000));
        } catch (Exception e) {
            //e.printStackTrace();
            countFrequencyLabel.setText("N/A");
        }
    }
}
