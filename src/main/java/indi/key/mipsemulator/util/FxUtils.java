package indi.key.mipsemulator.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FxUtils {
    private FxUtils() {
    }

    public static Stage newStage(Stage old, String title, String fxmlName,
                                 String cssName) {
        try {
            Stage stage = old;
            if (stage == null) {
                stage = new Stage();
            }
            stage.setTitle(title);
            Pane pane = FXMLLoader.load(FxUtils.class.getResource(
                    "/layout/" + fxmlName));
            Scene scene = new Scene(pane);
            if (cssName != null) {
                scene.getStylesheets().add(FxUtils.class.getResource(
                        "/layout/" + cssName).toExternalForm());
            }
            stage.getIcons().add(new Image(
                    FxUtils.class.getResourceAsStream("/drawable/sword_128.png")));
            stage.setScene(scene);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return old;
        }
    }

    public static void showException(Throwable throwable) {
        throwable.printStackTrace();
        Alert information = new Alert(Alert.AlertType.ERROR);
        information.setTitle(throwable.getClass().getSimpleName());
        information.setHeaderText(throwable.getMessage());
        information.showAndWait();
    }

    public static void showInfo(String info) {
        Alert information = new Alert(Alert.AlertType.INFORMATION);
        information.setHeaderText(info);
        information.showAndWait();
    }

    public static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }
}
