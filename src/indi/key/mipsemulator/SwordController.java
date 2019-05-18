package indi.key.mipsemulator;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.controller.ButtonController;
import indi.key.mipsemulator.controller.InputDialogController;
import indi.key.mipsemulator.controller.KeyboardController;
import indi.key.mipsemulator.controller.LedController;
import indi.key.mipsemulator.controller.MemoryController;
import indi.key.mipsemulator.controller.RegisterController;
import indi.key.mipsemulator.controller.SegmentController;
import indi.key.mipsemulator.controller.SwitchController;
import indi.key.mipsemulator.controller.VgaController;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.LogUtils;
import indi.key.mipsemulator.vga.VgaConfigures;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SwordController implements Initializable {

    public RadioMenuItem vgaEnglishMenu;
    public RadioMenuItem vgaChineseMenu;
    public RadioMenuItem vgaGraphMenu;
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

        debugText.setWrapText(true);
        LogUtils.setLogText(debugText);
        this.machine = Machine.getInstance(null);

        setUpControllers();
        setUpMenu();

        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> {
            Machine machine = Machine.getReference();
            if (machine != null && machine.isLooping()) {
                machine.exitLoop();
            }
        });
    }

    public void setUpMenu() {
        debugStopMenu.setDisable(true);
        VgaConfigures.setOnFontChangedCallback(() -> {
            if (VgaConfigures.isTextMode()) {
                VgaConfigures.Font font = VgaConfigures.getFont();
                vgaChineseMenu.setSelected(font == VgaConfigures.Font.ZH_16_16);
                vgaEnglishMenu.setSelected(font == VgaConfigures.Font.EN_8_8);
                vgaGraphMenu.setSelected(false);
            } else {
                vgaGraphMenu.setSelected(true);
                vgaChineseMenu.setSelected(false);
                vgaEnglishMenu.setSelected(false);
            }
        });
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

    public void onExit(ActionEvent actionEvent) {
        primaryStage.close();
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

    public void onEnglishMode(ActionEvent actionEvent) {
        VgaConfigures.setFont(VgaConfigures.Font.EN_8_8);
        VgaConfigures.setTextMode(true);
    }

    public void onChineseMode(ActionEvent actionEvent) {
        VgaConfigures.setFont(VgaConfigures.Font.ZH_16_16);
        VgaConfigures.setTextMode(true);
    }

    public void onGraphMode(ActionEvent actionEvent) {
        VgaConfigures.setTextMode(false);
    }

    public void onVgaModeControl(ActionEvent actionEvent) {
        InputDialogController.run(VgaConfigures.getModeRegister().toString(), s -> {
            try {
                BitArray bitArray = BitArray.of(IoUtils.parseUnsignedInteger(s), 32);
                VgaConfigures.setModeRegister(bitArray);
            } catch (Exception e) {
                LogUtils.m("Error occurs when modify VGA mode register: " + e.getMessage());
            }
        }, "修改VGA模式控制寄存器");
    }

    public void onVgaOffsetControl(ActionEvent actionEvent) {
        InputDialogController.run(VgaConfigures.getAddressOffsetRegister().toHexString(), s -> {
            try {
                BitArray bitArray = BitArray.of(IoUtils.parseUnsignedInteger(s), 32);
                VgaConfigures.setAddressOffsetRegister(bitArray);
            } catch (Exception e) {
                LogUtils.m("Error occurs when modify VRAM offset register: " + e.getMessage());
            }
        }, "修改VRAM起始地址");
    }
}
