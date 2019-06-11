package indi.key.mipsemulator;

import indi.key.mipsemulator.controller.stage.SwordController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SwordController.run(primaryStage);
    }

    public static void main(String[] args) {
        // UPDATE in v1.0.1: See https://stackoverflow.com/questions/31786980/javafx-combobox-not-responding-on-windows-10
        System.setProperty("glass.accessible.force", "false");
        launch(args);
    }
}
