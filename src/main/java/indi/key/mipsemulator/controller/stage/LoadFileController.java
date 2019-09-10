package indi.key.mipsemulator.controller.stage;

import com.sun.istack.internal.Nullable;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.util.FxUtils;
import indi.key.mipsemulator.util.SwordPrefs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class LoadFileController implements Initializable {

    private static FileLoadCallback callback;

    public TextField ramText;
    public Button ramButton;
    public TextField romText;
    public Button romButton;
    public Button loadButton;
    public CheckBox multiCheck;
    public CheckBox delaySlotCheck;
    public Label romLabel;

    private EventHandler<ActionEvent> onRomChecked = event -> {
        romText.setDisable(multiCheck.isSelected());
        romButton.setDisable(multiCheck.isSelected());
        romLabel.setDisable(multiCheck.isSelected());
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Machine machine = Machine.getInstance();
        bind(machine.getRamFile(), ramButton, SwordPrefs.DEFAULT_PATH, ramText);
        bind(machine.getRomFile(), romButton, SwordPrefs.DEFAULT_ROM_PATH, romText);
        multiCheck.setSelected(machine.getRomFile() == null);
        delaySlotCheck.setSelected(machine.isDelaySlotEnable());

        multiCheck.setOnAction(onRomChecked);
        onRomChecked.handle(null); // manual disable rom options

        loadButton.setOnAction(event -> {
            File rom = multiCheck.isSelected() ? null : new File(romText.getText());
            if (callback.onLoad(new File(ramText.getText()), rom, delaySlotCheck.isSelected())) {
                FxUtils.getStage(ramText).close();
            }
        });
    }

    private void bind(File originFile, Button selectButton, SwordPrefs defaultPath,
                      TextField fileText) {
        if (originFile != null) {
            fileText.setText(originFile.getAbsolutePath());
        }
        selectButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("COE文件", "*.coe"),
                    new FileChooser.ExtensionFilter("二进制文件", "*.*")
            );
            File selected;
            try {
                fileChooser.setInitialDirectory(new File(defaultPath.get()));
                selected = fileChooser.showOpenDialog(FxUtils.getStage(ramText));
            } catch (Exception e) {
                e.printStackTrace();
                // retry without initial directory
                fileChooser.setInitialDirectory(null);
                try {
                    selected = fileChooser.showOpenDialog(FxUtils.getStage(ramText));
                } catch (Exception e2) {
                    e2.printStackTrace();
                    selected = null;
                }
            }
            if (selected != null) {
                fileText.setText(selected.getAbsolutePath());
                defaultPath.set(selected.getParent());
            }
        });
    }

    public static void run(FileLoadCallback callback) {
        LoadFileController.callback = callback;
        FxUtils.newStage(null, "加载文件", "load_file.fxml", null).show();
    }

    public interface FileLoadCallback {
        boolean onLoad(File ram, @Nullable File rom, boolean useDelaySlot);
    }

}
