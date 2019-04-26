package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.storage.Register;

public interface ITypeAction extends Action {
    void execute(Machine m, Register rs, Register rt, BitArray immediate);
}
