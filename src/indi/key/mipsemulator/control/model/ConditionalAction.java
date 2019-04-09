package indi.key.mipsemulator.control.model;

import indi.key.mipsemulator.control.controller.Cpu;

public interface ConditionalAction extends Action {
    boolean check(Cpu cpu, Register rs, Register rt);
}
