package indi.key.mipsemulator.controller.stage;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.util.FxUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AboutController implements Initializable {
    private static final String VERSION_NAME = "1.2.3";
    public Hyperlink githubLink;
    public Label versionLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        versionLabel.setText(VERSION_NAME);
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
        Stage stage = FxUtils.newStage(null, "关于", "about.fxml", null);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.show();
    }
}
