package indi.key.mipsemulator.control.model;

import indi.key.mipsemulator.control.controller.Cpu;
import indi.key.mipsemulator.model.BitArray;

public interface ITypeAction extends Action {
    void execute(Cpu cpu, Register rs, Register rt, BitArray immediate);
}
