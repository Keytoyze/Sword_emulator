package indi.key.mipsemulator.controller;

import java.util.function.Function;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.controller.TimingRenderer;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class RegisterController implements TickCallback {

    public enum DisplayMode {
        HEXADECIMAL(integer -> "0x" + Integer.toHexString(integer).toUpperCase()),
        SIGNED_DECIMAL(Object::toString),
        UNSIGNED_DECIMAL(integer -> {
            return String.valueOf(Integer.toUnsignedLong(integer));
        });

        Function<Integer, String> convertFunc;

        DisplayMode(Function<Integer, String> convertFunc) {
            this.convertFunc = convertFunc;
        }
    }

    private GridPane registerPane;
    private Machine machine;
    private Label[] registerLable = new Label[32];
    private DisplayMode displayMode = DisplayMode.HEXADECIMAL;

    public RegisterController(GridPane registerPane, Machine machine) {
        this.registerPane = registerPane;
        this.machine = machine;
        //machine.addRegisterListener(this);
        initView();
        TimingRenderer.register(this);
    }

    public void setDisplayMode(DisplayMode displayMode) {
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                int index = j * 8 + i;
                RegisterType registerType = getType(index);
                registerLable[index] = new Label("");
//                registerLable[index].setFont(Font.font("Consolas", 16));
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
    public void onTick() {
        Platform.runLater(this::updateAllRegisters);
    }
}
