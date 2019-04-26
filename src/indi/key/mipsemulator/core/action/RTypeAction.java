package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.storage.Register;

public interface RTypeAction extends Action {
    void execute(Machine m, Register rs, Register rt, Register rd, Integer shamt);
}
