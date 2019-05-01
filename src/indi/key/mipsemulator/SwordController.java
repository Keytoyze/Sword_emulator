package indi.key.mipsemulator;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.controller.ButtonController;
import indi.key.mipsemulator.controller.KeyboardController;
import indi.key.mipsemulator.controller.LedController;
import indi.key.mipsemulator.controller.MemoryController;
import indi.key.mipsemulator.controller.RegisterController;
import indi.key.mipsemulator.controller.SegmentController;
import indi.key.mipsemulator.controller.SwitchController;
import indi.key.mipsemulator.controller.VgaController;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.util.LogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SwordController implements Initializable {

    @FXML
    TableView<MemoryController.MemoryBean> memoryTable;
    @FXML
    ComboBox<String> memroyTypeBox;
    @FXML
    TextField memoryAddressText;
    @FXML
    Button memoryJump;
    @FXML
    Button memoryLast;
    @FXML
    Button memoryNext;
    @FXML
    TextArea debugText;
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
    @FXML
    GridPane ledPane;
    @FXML
    GridPane buttonPane;
    @FXML
    Canvas segmentCanvas;
    @FXML
    Pane root;

    private Machine machine;
    private RegisterController registerController;
    private KeyboardController keyboardController;
    private VgaController vgaController;
    private SwitchController switchController;
    private LedController ledController;
    private ButtonController buttonController;
    private SegmentController segmentController;
    private MemoryController memoryController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LogUtils.i(location, resources);

        String path = "G:\\code\\java\\mipsasm\\mipsasm\\test\\computer_MCPU.bin";
        this.machine = Machine.getInstance(new File(path));

        setUpRegisters();
        setUpMenu();
        setUpVGA();
        setUpSlideSwitches();
        setUpLED();
        setUpButtons();
        setUpSegments();
        setUpMemory();
    }

    private void setUpRegisters() {
        registerController = new RegisterController(registerPane, machine);
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
            if (machine.getCpu().isLooping()) {
                LogUtils.i(machine.exitLoop());
                debugExecuteMenu.setText("运行");
            } else {
                machine.loop();
                debugExecuteMenu.setText("暂停");
            }
        });
        debugSingleIMenu.setOnAction(event -> machine.singleStep());
    }

    private void setUpSlideSwitches() {
        switchController = new SwitchController(
                slideSwitchGrid, slideLabelGrid, machine);
    }

    private void setUpLED() {
        ledController = new LedController(ledPane, machine);
    }

    private void setUpButtons() {
        buttonController = new ButtonController(buttonPane, machine);
    }

    private void setUpSegments() {
        segmentController = new SegmentController(segmentCanvas, machine);
    }

    private void setUpMemory() {
        memoryController = new MemoryController(memoryTable, machine, memoryJump, memoryLast,
                memoryNext, memroyTypeBox, memoryAddressText);
    }

    public void setUpVGA() {
        vgaController = new VgaController(vgaScreen, machine);
        keyboardController = new KeyboardController(machine);

        root.setOnKeyPressed(event -> {
            keyboardController.press(event.getCode());
        });
        root.setOnKeyReleased(event -> {
            keyboardController.release(event.getCode());
        });
    }

    public static void run(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ZJUQS-II SWORD Emulator");
        Pane pane = FXMLLoader.load(SwordController.class.getResource(
                "/res/layout/main.fxml"));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(SwordController.class.getResource(
                "/res/layout/main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
