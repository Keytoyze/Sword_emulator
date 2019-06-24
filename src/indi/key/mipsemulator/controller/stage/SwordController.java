package indi.key.mipsemulator.controller.stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.controller.component.ButtonController;
import indi.key.mipsemulator.controller.component.KeyboardController;
import indi.key.mipsemulator.controller.component.LedController;
import indi.key.mipsemulator.controller.component.RegisterController;
import indi.key.mipsemulator.controller.component.SegmentController;
import indi.key.mipsemulator.controller.component.SwitchController;
import indi.key.mipsemulator.controller.component.VgaController;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.util.FxUtils;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.LogUtils;
import indi.key.mipsemulator.vga.VgaConfigures;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SwordController implements Initializable {

    @FXML
    RadioButton graphModeButton;
    @FXML
    RadioButton chineseModeButton;
    @FXML
    RadioButton englishModeButton;
    @FXML
    Button pauseButton;
    @FXML
    Button singleButton;
    @FXML
    Button executeButton;
    @FXML
    RadioMenuItem vgaEnglishMenu;
    @FXML
    RadioMenuItem vgaChineseMenu;
    @FXML
    RadioMenuItem vgaGraphMenu;
    @FXML
    MenuItem debugStopMenu;
    @FXML
    MenuItem debugSingleMenu;
    @FXML
    MenuItem debugSingleWithoutJalMenu;
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
    private MemoryStageController memoryStageController;

    private static Stage primaryStage;
    private static Stage memoryStage;
    private static SwordController instance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        debugText.setWrapText(true);
        debugText.setFont(Font.font("Consolas", 20));
        Platform.runLater(() -> registerModeComboBox.requestFocus());
        LogUtils.setLogText(debugText);
        this.machine = Machine.getInstance(null);

        setUpControllers();
        setUpMenu();

        primaryStage.setOnCloseRequest(event -> {
            instance = null;
            primaryStage = null;
            if (memoryStage != null && memoryStage.isShowing()) {
                memoryStage.close();
            }
            Machine machine = Machine.getReference();
            if (machine != null && machine.isLooping()) {
                machine.exitLoop();
            }
        });
    }

    public static SwordController getInstance() {
        return instance;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setUpMenu() {
        debugStopMenu.setDisable(true);
        pauseButton.setDisable(true);
        VgaConfigures.setOnFontChangedCallback(() -> {
            if (VgaConfigures.isTextMode()) {
                VgaConfigures.Font font = VgaConfigures.getFont();
                vgaChineseMenu.setSelected(font == VgaConfigures.Font.ZH_16_16);
                vgaEnglishMenu.setSelected(font == VgaConfigures.Font.EN_8_8);
                vgaGraphMenu.setSelected(false);
                chineseModeButton.setSelected(font == VgaConfigures.Font.ZH_16_16);
                englishModeButton.setSelected(font == VgaConfigures.Font.EN_8_8);
                graphModeButton.setSelected(false);
            } else {
                vgaGraphMenu.setSelected(true);
                vgaChineseMenu.setSelected(false);
                vgaEnglishMenu.setSelected(false);
                graphModeButton.setSelected(true);
                chineseModeButton.setSelected(false);
                englishModeButton.setSelected(false);
            }
        });
    }

    private void setUpControllers() {
        registerController = new RegisterController(registerPane, registerModeComboBox, machine);
        switchController = new SwitchController(slideSwitchGrid, slideLabelGrid, machine);
        ledController = new LedController(ledPane, machine);
        buttonController = new ButtonController(buttonPane, machine);
        segmentController = new SegmentController(segmentCanvas, machine);
        vgaController = new VgaController(vgaScreen, machine);
        keyboardController = new KeyboardController(machine);
        debugText.setOnKeyPressed(event -> {
            if (machine.isLooping() && debugText.isFocused()) {
                keyboardController.press(event.getCode());
            } else {
                root.fireEvent(event);
            }
        });
        debugText.setOnKeyReleased(event -> {
            if (machine.isLooping() && debugText.isFocused()) {
                keyboardController.release(event.getCode());
            } else {
                root.fireEvent(event);
            }
        });
        debugText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) {
                LogUtils.m(newValue ? "Begin simulating the PS2 keyboard." : "Exit simulating the PS2 keyboard.");
            }
        });
        vgaScreen.setOnMouseClicked(event -> {
            debugText.requestFocus();
        });
    }

    public static void run(Stage primaryStage) {
        SwordController.primaryStage = primaryStage;
        FxUtils.newStage(primaryStage, "ZJUQS-II SWORD版模拟器",
                "main.fxml", "main.css");
        primaryStage.show();
    }

    public void onOpenFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("COE文件", "*.coe"),
                new FileChooser.ExtensionFilter("二进制文件", "*.*")
        );
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            onReset(null);
            onViewMemory(null);
            memoryStage.toBack();
            if (machine.isLooping()) {
                onPause(actionEvent);
            }
            try {
                machine = Machine.getInstance(file);
                LogUtils.m("Load file: " + file.getName());
            } catch (Exception e) {
                String[] reasons = e.getMessage().split(":");
                LogUtils.m("Fail to load file: " + file.getName() + ", reason: " +
                        reasons[reasons.length - 1]);
                machine = Machine.getInstance(null);
            }
            if (memoryStageController != null) {
                memoryStageController.refresh();
            }
        }
    }

    public void setMemoryStageController(MemoryStageController memoryStageController) {
        this.memoryStageController = memoryStageController;
    }

    public void onReset(ActionEvent actionEvent) {
        if (machine.isLooping()) {
            onPause(actionEvent);
        }
        machine.reset();
        vgaController.reset();
    }

    public void onExit(ActionEvent actionEvent) {
        primaryStage.close();
    }

    public void onExecute(Event actionEvent) {
        if (machine.loop()) {
            debugSingleMenu.setDisable(true);
            debugSingleWithoutJalMenu.setDisable(true);
            debugStopMenu.setDisable(false);
            debugExecuteMenu.setDisable(true);
            singleButton.setDisable(true);
            pauseButton.setDisable(false);
            executeButton.setDisable(true);
        }
    }

    public void onViewMemory(Event event) {
        if (memoryStage == null) {
            memoryStage = FxUtils.newStage(null, "内存查看",
                    "memory.fxml", "main.css");
        }
        memoryStage.show();
        memoryStage.toFront();
    }

    public void onSingle(ActionEvent actionEvent) {
        machine.singleStep();
        try {
            memoryStageController.jumpPcButton.fire();
        } catch (Exception ignore) {
        }
    }

    public void onSingleNotJal(ActionEvent actionEvent) {
        machine.singleStepWithoutJal();
    }

    public void onPause(ActionEvent actionEvent) {
        debugSingleMenu.setDisable(false);
        debugSingleWithoutJalMenu.setDisable(false);
        debugStopMenu.setDisable(true);
        debugExecuteMenu.setDisable(false);
        singleButton.setDisable(false);
        pauseButton.setDisable(true);
        executeButton.setDisable(false);
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
                LogUtils.m("Error occurs when modify VRAM_GRAPH offset register: " + e.getMessage());
            }
        }, "修改VRAM起始地址");
    }

    public void onAbout(ActionEvent actionEvent) {
        AboutController.run();
    }

    public void onCustomAddress(ActionEvent actionEvent) {
        FxUtils.newStage(null, "自定义总线外设地址", "address.fxml", null)
                .show();
    }
}
