/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package indi.key.mipsemulator.disassemble;

import indi.key.mipsemulator.core.model.Statement;

public class OperandPrototypes {

    private OperandPrototypes() {}

    public static final OperandPrototype DESTINATION = OperandPrototype.of(Statement::getRd, OperandType.REGISTER);

    public static final OperandPrototype SOURCE2 = OperandPrototype.of(Statement::getRt, OperandType.REGISTER);

    public static final OperandPrototype SOURCE = OperandPrototype.of(Statement::getRs, OperandType.REGISTER);

    public static final OperandPrototype IMMEDIATE = OperandPrototype.of(Statement::getImmediate, OperandType.IMMEDIATE);

    public static final OperandPrototype OFFSET = OperandPrototype.of(Statement::getImmediate, OperandType.OFFSET);

    public static final OperandPrototype COPROCESSOR_FUNCTION = OperandPrototype.of(Statement::getCofun, OperandType.COPROCESSOR_FUNCTION);

    public static final OperandPrototype SHIFT_AMOUNT = OperandPrototype.of(Statement::getShamt, OperandType.SHIFT_AMOUNT);

    public static final OperandPrototype TARGET = OperandPrototype.of(Statement::getAddress, OperandType.TARGET);

    public static final OperandPrototype OFFSET_BASE = OperandPrototype.of(null, OperandType.OFFSET_BASE);

}
