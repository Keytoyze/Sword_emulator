package indi.key.mipsemulator.controller.stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import indi.key.mipsemulator.util.LogUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        try {
            Stage stage = new Stage();
            stage.setMaximized(false);
            mDefaultString = defaultString;
            callbacks.put(stage, returnCallback);
            Parent root = FXMLLoader.load(InputDialogController.class.getResource("/res/layout/simple_input_dialog.fxml"));
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.m(e.toString());
        }
    }
}
