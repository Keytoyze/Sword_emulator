package indi.key.mipsemulator.controller.instruction;

import indi.key.mipsemulator.controller.emulator.Cpu;
import indi.key.mipsemulator.controller.emulator.Statement;

public interface JumpAction extends Action {
    int getNext(Cpu cpu, Statement statement);
}
