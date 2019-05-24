package indi.key.mipsemulator.controller.stage;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.util.LogUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class AboutController implements Initializable {
    public Hyperlink githubLink;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        githubLink.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new
                        URI("https://github.com/Keytoyze/Sword_emulator"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void run() {
        try {
            Stage stage = new Stage();
            stage.setMaximized(false);
            stage.setResizable(false);
            Parent root = FXMLLoader.load(InputDialogController.class.getResource("/res/layout/about.fxml"));
            stage.setTitle("关于");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.m(e.toString());
        }
    }
}
