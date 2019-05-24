package indi.key.mipsemulator.controller;

import java.util.function.Function;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.controller.TimingRenderer;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import indi.key.mipsemulator.util.IoUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class RegisterController implements TickCallback {

    public enum DisplayMode {
        HEXADECIMAL("十六进制", integer -> "0x" + IoUtils.completeWithZero(Integer.toHexString(integer).toUpperCase(), 8)),
        SIGNED_DECIMAL("有符号十进制", Object::toString),
        UNSIGNED_DECIMAL("无符号十进制", integer -> String.valueOf(Integer.toUnsignedLong(integer)));


        Function<Integer, String> convertFunc;
        String name;

        DisplayMode(String name, Function<Integer, String> convertFunc) {
            this.name = name;
            this.convertFunc = convertFunc;
        }
    }

    private GridPane registerPane;
    private ComboBox<String> registerModeBox;
    private Machine machine;
    private Label[] registerLable = new Label[32];
    private DisplayMode displayMode = DisplayMode.HEXADECIMAL;

    public RegisterController(GridPane registerPane, ComboBox<String> registerModeBox, Machine machine) {
        this.registerPane = registerPane;
        this.machine = machine;
        this.registerModeBox = registerModeBox;
        //machine.addRegisterListener(this);
        initView();
        TimingRenderer.register(this);
    }

    private void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
        updateAllRegisters();
    }

    private void updateAllRegisters() {
        for (int i = 0; i < 32; i++) {
            RegisterType registerType = getType(i);
            updateRegisterValue(machine.getRegister(registerType));
        }
    }

    private void initView() {
        ObservableList<String> options = FXCollections.observableArrayList();
        for (DisplayMode mode : DisplayMode.values()) {
            options.add(mode.name);
        }
        registerModeBox.setItems(options);
        registerModeBox.setOnAction(event ->
                setDisplayMode(DisplayMode.values()[registerModeBox.getSelectionModel().getSelectedIndex()]));
        registerModeBox.getSelectionModel().select(0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                int index = j * 8 + i;
                RegisterType registerType = getType(index);
                registerLable[index] = new Label("");
                registerLable[index].setFont(Font.font("Consolas", 16));
                registerLable[index].setTextFill(registerType.getColor());
                registerPane.add(registerLable[index], j, i);
            }
        }
        updateAllRegisters();
    }

    private static RegisterType getType(int index) {
        if (index == 0) return RegisterType.PC;
        return RegisterType.of(index);
    }

    private static int getIndex(RegisterType registerType) {
        if (registerType == RegisterType.PC) return 0;
        int ordinal = registerType.ordinal();
        if (ordinal <= 32 && ordinal > 0) {
            return ordinal;
        }
        return -1;
    }

    private void updateRegisterValue(Register register) {
        RegisterType registerType = register.getRegisterType();
        int index = getIndex(registerType);
        if (index != -1) {
            registerLable[index].setText(registerType + ": " +
                    displayMode.convertFunc.apply(register.get()));
        }
    }

    @Override
    public void onTick(long ticks) {
        Platform.runLater(this::updateAllRegisters);
    }
}
