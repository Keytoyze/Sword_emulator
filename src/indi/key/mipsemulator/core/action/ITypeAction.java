package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.storage.Register;

public interface ITypeAction extends Action {
    void execute(Cpu cpu, Register rs, Register rt, BitArray immediate);
}
