package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.storage.Register;

public interface ITypeAction extends Action {
    void execute(Machine m, Register rs, Register rt, BitArray immediate);

    @Override
    default void execute(Machine m, Cpu c, Statement s) {
        this.execute(m, s.rsReg, s.rtReg, s.immediate);
    }
}
