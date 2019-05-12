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
import javafx.event.ActionEvent;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SwordController implements Initializable {

    @FXML
    MenuItem debugStopMenu;
    @FXML
    MenuItem debugSingleMenu;
    @FXML
    MenuItem debugSingleWithoutJalMenu;
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

    private static Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LogUtils.setLogText(debugText);
        this.machine = Machine.getInstance(null);

        setUpControllers();
        setUpMenu();

        primaryStage.setOnCloseRequest(event -> {
            Machine machine = Machine.getReference();
            if (machine != null && machine.isLooping()) {
                machine.exitLoop();
            }
        });
    }

    public void setUpMenu() {
        debugStopMenu.setDisable(true);/*
        debugExecuteMenu.setOnAction(event -> {
            if (machine.getCpu().isLooping()) {
                machine.exitLoop().print();
                debugExecuteMenu.setText("运行");
            } else {
                machine.loop();
                debugExecuteMenu.setText("暂停");
            }
        });
        debugExecuteMenu.setAccelerator(new KeyCodeCombination(KeyCode.F4));
        debugSingleIMenu.setOnAction(event -> machine.singleStep());
        debugSingleIMenu.setAccelerator(new KeyCodeCombination(KeyCode.F5));
    */
    }

    private void setUpControllers() {
        registerController = new RegisterController(registerPane, registerModeComboBox, machine);
        switchController = new SwitchController(slideSwitchGrid, slideLabelGrid, machine);
        ledController = new LedController(ledPane, machine);
        buttonController = new ButtonController(buttonPane, machine);
        segmentController = new SegmentController(segmentCanvas, machine);
        memoryController = new MemoryController(memoryTable, machine, memoryJump, memoryLast,
                memoryNext, memroyTypeBox, memoryAddressText);
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
        SwordController.primaryStage = primaryStage;
        primaryStage.setTitle("ZJUQS-II SWORD Emulator");
        Pane pane = FXMLLoader.load(SwordController.class.getResource(
                "/res/layout/main.fxml"));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(SwordController.class.getResource(
                "/res/layout/main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void onOpenFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("COE文件", "*.coe"),
                new FileChooser.ExtensionFilter("BIN文件", "*.bin")
        );
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            machine = Machine.getInstance(file);
        }
    }

    public void onReset(ActionEvent actionEvent) {
        machine.reset();
    }

    public void onSaveMemory(ActionEvent actionEvent) {

    }

    public void onExit(ActionEvent actionEvent) {

    }

    public void onExecute(ActionEvent actionEvent) {
        debugSingleMenu.setDisable(true);
        debugSingleWithoutJalMenu.setDisable(true);
        debugStopMenu.setDisable(false);
        debugExecuteMenu.setDisable(true);
        machine.loop();
    }

    public void onSingle(ActionEvent actionEvent) {
        machine.singleStep();
    }

    public void onSingleNotJal(ActionEvent actionEvent) {
        machine.singleStepWithoutJal();
    }

    public void onPause(ActionEvent actionEvent) {
        debugSingleMenu.setDisable(false);
        debugSingleWithoutJalMenu.setDisable(false);
        debugStopMenu.setDisable(true);
        debugExecuteMenu.setDisable(false);
        machine.exitLoop().print();
    }
}
