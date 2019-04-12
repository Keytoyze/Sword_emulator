package indi.key.mipsemulator;

import java.io.File;

import indi.key.mipsemulator.control.controller.Cpu;
import indi.key.mipsemulator.keyboard.PS2Key;
import indi.key.mipsemulator.model.BitArray;
import indi.key.mipsemulator.util.LogUtils;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setTitle("FXML TableView Example");
//        Pane myPane = FXMLLoader.load(getClass().getResource(
//                "/res/layout/main.fxml"));
//        Scene myScene = new Scene(myPane);
//        primaryStage.setScene(myScene);
//        primaryStage.show();
        TextField tf = new TextField();


        Scene scene = new Scene(tf, 300, 250);
        tf.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.print(event.getCode() + " t, ");
            }
        });
        tf.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                System.out.print(event.getCode() + " i, ");
            }
        });
        tf.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(BitArray.of(PS2Key.of(event.getCode()).getPressCode()).toHexString() + " o");
            }
        });
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        String path = "G:\\code\\java\\mipsasm\\mipsasm\\test\\computer_MCPU.bin";
        Cpu cpu = new Cpu(new File(path));
        //long time = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            try {
                cpu.singleStep();
            } catch (Exception e) {
                LogUtils.i(e);
                //e.printStackTrace();
            }

        }

        //System.out.println(System.currentTimeMillis() - time);
//        // 1953 1878 2090 2043 1959
//        // 2280 2216 2193
//        // 205
    }
}
