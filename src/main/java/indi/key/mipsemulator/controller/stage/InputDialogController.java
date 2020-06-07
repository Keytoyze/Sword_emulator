package indi.key.mipsemulator.controller.stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import indi.key.mipsemulator.util.FxUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InputDialogController implements Initializable {

    private static Map<Stage, String> defaultStrings = new HashMap<>();
    private static Map<Stage, InputCallback> callbacks = new HashMap<>();
    @FXML
    TextField inputText;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            Stage stage = FxUtils.getStage(inputText);
            inputText.setText(defaultStrings.get(stage));
            inputText.selectAll();
            stage.setOnCloseRequest(event -> {
                defaultStrings.remove(stage);
                callbacks.remove(stage);
            });
        });
        okButton.setOnAction(event -> {
            Stage stage = FxUtils.getStage(okButton);
            try {
                callbacks.get(stage).accept(inputText.getText());
                stage.close();
            } catch (Exception e) {
                FxUtils.showException(e);
            }
        });
        cancelButton.setOnAction(event -> {
            Stage stage = FxUtils.getStage(cancelButton);
            stage.close();
        });
    }

    public static void run(String defaultString, InputCallback returnCallback, String title) {
        Stage stage = FxUtils.newStage(null, title, "simple_input_dialog.fxml", null);
        defaultStrings.put(stage, defaultString);
        callbacks.put(stage, returnCallback);
        stage.show();
    }

    public interface InputCallback {
        void accept(String content) throws Exception;
    }
}
