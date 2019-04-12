package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.storage.Register;

public interface RTypeAction extends Action {
    void execute(Cpu cpu, Register rs, Register rt, Register rd, Integer shamt);
}
