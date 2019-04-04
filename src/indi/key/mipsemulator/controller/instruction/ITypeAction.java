package indi.key.mipsemulator.controller.instruction;

import indi.key.mipsemulator.controller.emulator.Cpu;
import indi.key.mipsemulator.model.storage.Register;
import indi.key.mipsemulator.util.BitArray;

public interface ITypeAction extends Action {
    void execute(Cpu cpu, Register rs, Register rt, BitArray immediate);
}
