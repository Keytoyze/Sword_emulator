package indi.key.mipsemulator;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.controller.KeyboardController;
import indi.key.mipsemulator.controller.RegisterController;
import indi.key.mipsemulator.controller.SlideSwitchController;
import indi.key.mipsemulator.controller.VgaController;
import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.util.LogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SwordController implements Initializable {

    @FXML
    GridPane registerPane;
    @FXML
    ImageView vgaScreen;
    @FXML
    MenuItem debugExecuteMenu;
    @FXML
    MenuItem debugSingleIMenu;
    @FXML
    ComboBox<String> registerModeComboBox;
    @FXML
    GridPane slideSwitchGrid;
    @FXML
    GridPane slideLabelGrid;

    private Cpu cpu;
    private RegisterController registerController;
    private KeyboardController keyboardController;
    private VgaController vgaController;
    private SlideSwitchController slideSwitchController;

    @FXML
    Pane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LogUtils.i(location, resources);

        String path = "G:\\code\\java\\mipsasm\\mipsasm\\test\\computer_MCPU.bin";
        cpu = new Cpu(new File(path));

        setUpRegisters();
        setUpMenu();
        setUpVGA();
        setUpSlideSwitches();
    }

    private void setUpRegisters() {
        registerController = new RegisterController(registerPane, cpu);
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "十六进制",
                        "有符号十进制",
                        "无符号十进制"
                );
        registerModeComboBox.setItems(options);
        registerModeComboBox.setOnAction(event -> {
            switch (registerModeComboBox.getSelectionModel().getSelectedIndex()) {
                case 0:
                    registerController.setDisplayMode(RegisterController.DisplayMode.HEXADECIMAL);
                    break;
                case 1:
                    registerController.setDisplayMode(RegisterController.DisplayMode.SIGNED_DECIMAL);
                    break;
                case 2:
                    registerController.setDisplayMode(RegisterController.DisplayMode.UNSIGNED_DECIMAL);
                    break;
            }
        });
        registerModeComboBox.setValue(registerModeComboBox.getItems().get(0));
    }

    public void setUpMenu() {
        debugExecuteMenu.setOnAction(event -> {
            if (cpu.isLooping()) {
                LogUtils.i(cpu.exitLoop());
                debugExecuteMenu.setText("运行");
            } else {
                cpu.loop();
                debugExecuteMenu.setText("暂停");
            }
        });
        debugSingleIMenu.setOnAction(event -> cpu.singleStep());
    }

    private void setUpSlideSwitches() {
        slideSwitchController = new SlideSwitchController(
                slideSwitchGrid, slideLabelGrid);
    }

    public void setUpVGA() {
        vgaController = new VgaController(vgaScreen, cpu);
        keyboardController = new KeyboardController(cpu);

        root.setOnKeyPressed(event -> {
            keyboardController.press(event.getCode());
        });
        root.setOnKeyReleased(event -> {
            keyboardController.release(event.getCode());
        });
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
