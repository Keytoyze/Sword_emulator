package indi.key.mipsemulator.controller.stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import indi.key.mipsemulator.util.FxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InputDialogController implements Initializable {

    private static String mDefaultString;
    private static Map<Stage, Consumer<String>> callbacks = new HashMap<>();
    @FXML
    TextField inputText;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inputText.setText(mDefaultString);
        okButton.setOnAction(event -> {
            Stage stage = (Stage) okButton.getScene().getWindow();
            callbacks.get(stage).accept(inputText.getText());
            stage.close();
        });
        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        });
    }

    public static void run(String defaultString, Consumer<String> returnCallback, String title) {
        Stage stage = FxUtils.newStage(null, title, "simple_input_dialog.fxml", null);
        mDefaultString = defaultString;
        callbacks.put(stage, returnCallback);
        stage.show();
    }
}
