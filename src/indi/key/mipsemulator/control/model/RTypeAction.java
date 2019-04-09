package indi.key.mipsemulator.control.model;

import indi.key.mipsemulator.control.controller.Cpu;

public interface RTypeAction extends Action {
    void execute(Cpu cpu, Register rs, Register rt, Register rd, Integer shamt);
}
