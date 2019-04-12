package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.storage.Register;

public interface ConditionalAction extends Action {
    boolean check(Cpu cpu, Register rs, Register rt);
}
