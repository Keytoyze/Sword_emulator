package indi.key.mipsemulator.model.interfaces;

import indi.key.mipsemulator.storage.Register;

public interface CpuCallback {
    void onCpuNext(Register pc);
}
