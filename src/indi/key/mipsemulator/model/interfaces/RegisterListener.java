package indi.key.mipsemulator.model.interfaces;

import indi.key.mipsemulator.storage.Register;

public interface RegisterListener {
    void onRegisterChange(Register register);
}
