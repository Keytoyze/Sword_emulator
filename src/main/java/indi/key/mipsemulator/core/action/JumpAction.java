package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.model.Statement;

public interface JumpAction extends Action {
    int getNext(Machine m, Statement statement);

    @Override
    default void execute(Machine m, Cpu c, Statement s) {
        c.changeAddress(s.instruction.getDelaySlotType(), getNext(m, s));
    }
}
