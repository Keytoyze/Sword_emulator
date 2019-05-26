package indi.key.mipsemulator.util;

import com.sun.istack.internal.Nullable;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FxUtils {
    private FxUtils() {
    }

    public static Stage newStage(@Nullable Stage old, String title, String fxmlName,
                                 @Nullable String cssName) {
        try {
            Stage stage = old;
            if (stage == null) {
                stage = new Stage();
            }
            stage.setTitle(title);
            Pane pane = FXMLLoader.load(FxUtils.class.getResource(
                    "/res/layout/" + fxmlName));
            Scene scene = new Scene(pane);
            if (cssName != null) {
                scene.getStylesheets().add(FxUtils.class.getResource(
                        "/res/layout/" + cssName).toExternalForm());
            }
            stage.getIcons().add(new Image(
                    FxUtils.class.getResourceAsStream("/res/drawable/sword_128.png")));
            stage.setScene(scene);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return old;
        }
    }

    public static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }
}
