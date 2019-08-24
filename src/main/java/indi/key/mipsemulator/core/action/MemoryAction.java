package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.storage.Register;

// NOTE: Actually it should be regarded as the subclass of RTypeAction.
// But we want to use beautiful lambda expression in the Instruction, which
// means this class should be an interface rather than an abstract class.
public interface MemoryAction extends Action {
    void execute(Machine m, long address, Register rt);
}
