package indi.key.mipsemulator.model;

import indi.key.mipsemulator.storage.Register;

public interface RegisterListener {
    void onRegisterChange(Register register);
}
