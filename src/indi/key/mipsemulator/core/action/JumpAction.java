package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.core.model.Statement;

public interface JumpAction extends Action {
    int getNext(Cpu cpu, Statement statement);
}
