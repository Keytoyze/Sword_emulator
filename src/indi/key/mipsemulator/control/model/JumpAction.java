package indi.key.mipsemulator.control.model;

import indi.key.mipsemulator.control.controller.Cpu;
import indi.key.mipsemulator.control.controller.Statement;

public interface JumpAction extends Action {
    int getNext(Cpu cpu, Statement statement);
}
