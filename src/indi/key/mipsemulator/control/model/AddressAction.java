package indi.key.mipsemulator.control.model;

import indi.key.mipsemulator.control.controller.Cpu;

public interface AddressAction extends Action {
    void execute(Cpu cpu, long address, Register rt);
}
