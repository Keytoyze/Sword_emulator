package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.storage.Register;

public interface ConditionalAction extends Action {
    boolean check(Machine m, Register rs, Register rt);
}
