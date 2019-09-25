package indi.key.mipsemulator.core.action;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.storage.Register;

public interface BranchAction extends Action {
    boolean check(Machine m, Register rs, Register rt);

    @Override
    default void execute(Machine m, Cpu c, Statement s) {
        // FIXME
        boolean check = check(m, s.rsReg, s.rtReg);
        if (check) {
            c.changeAddress(s.instruction.getDelaySlotType(), c.getPc() + s.immediate.integerValue() * 4);
        }
    }
}
