package indi.key.mipsemulator;

import indi.key.mipsemulator.controller.SwordController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SwordController.run(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
