/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package indi.key.mipsemulator.disassemble;

public class OperandPrototypes {

    private OperandPrototypes() {}

    public static final OperandPrototype DESTINATION = OperandPrototype.of(s -> s.rd, OperandType.REGISTER);

    public static final OperandPrototype SOURCE2 = OperandPrototype.of(s -> s.rt, OperandType.REGISTER);

    public static final OperandPrototype SOURCE = OperandPrototype.of(s -> s.rs, OperandType.REGISTER);

    public static final OperandPrototype IMMEDIATE = OperandPrototype.of(s -> s.immediate, OperandType.IMMEDIATE);

    public static final OperandPrototype OFFSET = OperandPrototype.of(s -> s.immediate, OperandType.OFFSET);

    public static final OperandPrototype COPROCESSOR_FUNCTION = OperandPrototype.of(s -> s.cofun, OperandType.COPROCESSOR_FUNCTION);

    public static final OperandPrototype SHIFT_AMOUNT = OperandPrototype.of(s -> s.shamt, OperandType.SHIFT_AMOUNT);

    public static final OperandPrototype TARGET = OperandPrototype.of(s -> s.address, OperandType.TARGET);

    public static final OperandPrototype OFFSET_BASE = OperandPrototype.of(null, OperandType.OFFSET_BASE);

}
