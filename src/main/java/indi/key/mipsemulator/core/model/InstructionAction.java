package indi.key.mipsemulator.core.model;

import java.util.function.BooleanSupplier;

public class InstructionAction {
    public Runnable actualAction;
    public int delaySlotType;
    // valid only if delaySlotType is DelaySlotType.LIKELY
    public BooleanSupplier shouldExecuteDelaySlot;

    public InstructionAction(Runnable actualAction, int delaySlotType) {
        this.actualAction = actualAction;
        this.delaySlotType = delaySlotType;
        this.shouldExecuteDelaySlot = null;
    }

    public InstructionAction(Runnable actualAction, BooleanSupplier shouldExecuteDelaySlot) {
        this.actualAction = actualAction;
        this.delaySlotType = DelaySlotType.LIKELY;
        this.shouldExecuteDelaySlot = shouldExecuteDelaySlot;
    }


}
