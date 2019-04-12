package indi.key.mipsemulator;

import indi.key.mipsemulator.keyboard.PS2Key;
import indi.key.mipsemulator.model.BitArray;
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
        launch(args);
//        for (KeyCode d : KeyCode.values()) {
//            System.out.println(d.name());
//        }
//        String path = "G:\\code\\java\\mipsasm\\mipsasm\\test\\computer_MCPU.bin";
//        RAM ram = new RAM(65536);
//        Rom rom = new Rom(new File(path));
//        Cpu cpu = new Cpu(ram, rom);
//        //long time = System.currentTimeMillis();
//        cpu.loop();
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException ignore) {
//
//        }
//
//        System.out.println(cpu.exitLoop().toString());
//        //System.out.println(System.currentTimeMillis() - time);
//        // 1953 1878 2090 2043 1959
//        // 2280 2216 2193
//        // 205
    }
}
