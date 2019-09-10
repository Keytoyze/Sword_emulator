package indi.key.mipsemulator.controller.stage;

import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.controller.component.MemoryController;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.util.FxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MemoryStageController implements Initializable {

    @FXML
    Button jumpPcButton;
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

    private MemoryController memoryController;

    private static MemoryStageController ramViewer = null, romViewer = null;
    private static Stage ramStage = null, romStage = null;
    private static boolean isViewingRam = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SwordController swordController = SwordController.getInstance();
        if (swordController != null) {
            Machine machine = swordController.getMachine();
            memoryController = new MemoryController(memoryTable, machine,
                    memoryJump, memoryLast, memoryNext,
                    memroyTypeBox, memoryAddressText, 25,
                    isViewingRam
                            ? machine.getAddressRedirector()
                            : machine.getRom()
            );
            if (isViewingRam) {
                ramViewer = this;
            } else {
                romViewer = this;
            }
            memoryAddressText.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER:
                        memoryJump.fire();
                        break;
                    case PAGE_DOWN:
                        memoryNext.fire();
                        break;
                    case PAGE_UP:
                        memoryLast.fire();
                        break;
                }
            });

            jumpPcButton.setDisable(isViewingRam && machine.getRom() != null);
            jumpPcButton.setOnAction(event -> memoryController.jumpToPc());
        }
    }

    public static void refreshAll() {
        if (ramViewer != null) {
            ramViewer.memoryController.refresh();
            ramViewer.jumpPcButton.setDisable(Machine.getInstance().getRom() != null);
        }
        if (romViewer != null) {
            if (Machine.getInstance().getRom() != null) {
                romViewer.memoryController.refresh();
            } else {
                romStage.close();
                romViewer = null;
                romStage = null;
            }
        }
    }

    public static void closeAll() {
        if (ramStage != null && ramStage.isShowing()) {
            ramStage.close();
        }
        if (romStage != null && romStage.isShowing()) {
            romStage.close();
        }
    }

    public static void viewMemory(boolean isRam) {
        isViewingRam = isRam;
        Stage stage;
        if (isViewingRam) {
            if (ramStage == null) {
                ramStage = genStage("RAM");
            }
            stage = ramStage;
        } else {
            if (romStage == null) {
                romStage = genStage("ROM");
            }
            stage = romStage;
        }
        stage.show();
        stage.toFront();
    }

    private static Stage genStage(String type) {
        return FxUtils.newStage(null, "内存查看 (" + type + ")",
                "memory.fxml", "main.css");
    }

    public static void jumpToPC() {
        if (romViewer != null) {
            romViewer.jumpPcButton.fire();
        }
        if (ramViewer != null && !ramViewer.jumpPcButton.isDisabled()) {
            ramViewer.jumpPcButton.fire();
        }
    }

    public void jumpTo(long addr) {
        memoryController.jumpTo(addr);
    }

}
