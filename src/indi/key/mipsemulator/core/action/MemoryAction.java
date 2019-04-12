package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.storage.Register;

public interface MemoryAction extends Action {
    void execute(Cpu cpu, long address, Register rt);
}
