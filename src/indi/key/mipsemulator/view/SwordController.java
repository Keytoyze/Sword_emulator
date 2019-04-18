package indi.key.mipsemulator.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.util.LogUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SwordController implements Initializable {

    @FXML
    GridPane registerPane;
    @FXML
    ImageView vgaScreen;

    private Cpu cpu;
    private RegisterController registerController;
    private WritableImage writableImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LogUtils.i(location, resources);

        String path = "G:\\code\\java\\mipsasm\\mipsasm\\test\\computer_MCPU.bin";
        cpu = new Cpu(new File(path));


        registerController = new RegisterController(registerPane, cpu);


        writableImage = new WritableImage(640, 480);
        byte[] d = new byte[3100000 * 4];


        for (int i = 0; i < 640 * 480; i++) {
            int x = i % 640;
            int y = i / 640;

            d[i * 4] = (x * x + y * y <= 90000) ? (byte) 255 : 0;
            d[i * 4 + 1] = 0;
            d[i * 4 + 2] = 0;
            d[i * 4 + 3] = (byte) 255;
        }
        writableImage.getPixelWriter().setPixels(0, 0, 640, 480, PixelFormat.getByteBgraPreInstance()
                , d, 0, 640 * 4);

        vgaScreen.setImage(writableImage);
    }

    public static void run(Stage primaryStage) throws Exception {
        LogUtils.i(primaryStage);
        primaryStage.setTitle("ZJUQS-II SWORD Emulator");
        Pane pane = FXMLLoader.load(SwordController.class.getResource(
                "/res/layout/main.fxml"));
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
