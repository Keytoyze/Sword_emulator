package indi.key.mipsemulator;

import indi.key.mipsemulator.view.SwordController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SwordController.run(primaryStage);

//        TextField tf = new TextField();
//
//
//        Scene scene = new Scene(tf, 300, 250);
//        tf.setOnKeyTyped(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                System.out.print(event.getCode() + " t, ");
//            }
//        });
//        tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
//
//            @Override
//            public void handle(KeyEvent event) {
//                System.out.print(event.getCode() + " i, ");
//            }
//        });
//        tf.setOnKeyReleased(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                System.out.println(BitArray.of(PS2Key.of(event.getCode()).getPressCode()).toHexString() + " o");
//            }
//        });
//        primaryStage.setTitle("Hello World!");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
//        String path = "G:\\code\\java\\mipsasm\\mipsasm\\test\\computer_MCPU.bin";
//        Cpu cpu = new Cpu(new File(path));
//        //long time = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            try {
//                cpu.singleStep();
//            } catch (Exception e) {
//                LogUtils.i(e);
//                //e.printStackTrace();
//            }
//
//        }

    }
}
