package indi.key.mipsemulator.controller.stage;

import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.controller.component.MemoryController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SwordController swordController = SwordController.getInstance();
        if (swordController != null) {
            memoryController = new MemoryController(memoryTable, swordController.getMachine(),
                    memoryJump, memoryLast,
                    memoryNext, memroyTypeBox, memoryAddressText, 25);
            swordController.setMemoryStageController(this);
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
            jumpPcButton.setOnAction(event -> {
                memoryController.jumpToPc();
            });
        }
    }

    public void refresh() {
        memoryController.refresh();
    }

    public void jumpTo(long addr) {
        memoryController.jumpTo(addr);
    }

}
