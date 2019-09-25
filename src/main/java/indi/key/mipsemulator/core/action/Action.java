package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.exception.NotImplementedException;

public interface Action {
    default void execute(Machine m, Cpu c, Statement s) {
        throw new NotImplementedException("Action " + s.instruction.name() + " haven't been implemented.");
    }
}
