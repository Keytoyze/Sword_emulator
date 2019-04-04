package indi.key.mipsemulator.controller.instruction;

import indi.key.mipsemulator.controller.emulator.Cpu;

public interface JTypeAction extends Action {
    void execute(Cpu cpu, Integer address);
}
