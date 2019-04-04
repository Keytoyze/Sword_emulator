package indi.key.mipsemulator.controller.instruction;

import indi.key.mipsemulator.controller.emulator.Cpu;
import indi.key.mipsemulator.model.storage.Register;

public interface ConditionalAction extends Action {
    boolean check(Cpu cpu, Register rs, Register rt);
}
