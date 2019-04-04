package indi.key.mipsemulator.controller.instruction;

import indi.key.mipsemulator.controller.emulator.Cpu;
import indi.key.mipsemulator.model.storage.Register;

public interface RTypeAction extends Action {
    void execute(Cpu cpu, Register rs, Register rt, Register rd, Integer shamt);
}
