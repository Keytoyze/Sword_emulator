package indi.key.mipsemulator.view;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.model.interfaces.RegisterListener;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

class RegisterController implements RegisterListener {

    private GridPane registerPane;
    private Cpu cpu;
    private Label[] registerLable = new Label[32];

    RegisterController(GridPane registerPane, Cpu cpu) {
        this.registerPane = registerPane;
        this.cpu = cpu;
        cpu.setRegisterListener(this);
        initView();
    }

    private void initView() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                int index = j * 8 + i;
                RegisterType registerType = getType(index);
                registerLable[index] = new Label("");
                registerLable[index].setTextFill(registerType.getColor());
                registerPane.add(registerLable[index], j, i);
                updateRegisterValue(cpu.getRegister(registerType));
            }
        }
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
            registerLable[index].setText(registerType + ": " + register.get());
        }
    }

    @Override
    public void onRegisterChange(Register register) {
        updateRegisterValue(register);
    }
}
