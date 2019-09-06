package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.model.Statement;

public interface JumpAction extends Action {
    int getNext(Machine m, Statement statement);
}
