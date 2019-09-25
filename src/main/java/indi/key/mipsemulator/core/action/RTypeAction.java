package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.storage.Register;

public interface RTypeAction extends Action {
    void execute(Machine m, Register rs, Register rt, Register rd, int shamt);

    @Override
    default void execute(Machine m, Cpu c, Statement s) {
        this.execute(m, s.rsReg, s.rtReg, s.rdReg, s.shamt.value());
    }
}
