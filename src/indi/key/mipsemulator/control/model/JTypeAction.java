package indi.key.mipsemulator.control.model;

import indi.key.mipsemulator.control.controller.Cpu;

public interface JTypeAction extends Action {
    void execute(Cpu cpu, Integer address);
}
