/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package indi.key.mipsemulator.disassemble;

import java.util.function.BiFunction;

import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.storage.RegisterType;

public enum OperandType {

    REGISTER((bitArray, statement) -> RegisterType.of(bitArray.value()).toString()),
    IMMEDIATE((bitArray, statement) -> String.valueOf(bitArray.integerValue())),
    OFFSET((bitArray, statement) -> String.valueOf(bitArray.integerValue())),
    COPROCESSOR_FUNCTION((bitArray, statement) -> bitArray.toHexString()),
    SHIFT_AMOUNT((bitArray, statement) -> String.valueOf(bitArray.integerValue())),
    TARGET((bitArray, statement) -> bitArray.toHexString()),
    OFFSET_BASE((bitArray, statement) ->
            statement.getImmediate().integerValue() + "(" +
                    RegisterType.of(statement.getRs()) + ")");

    BiFunction<BitArray, Statement, String> disassembler;

    OperandType(BiFunction<BitArray, Statement, String> disassembler) {
        this.disassembler = disassembler;
    }

    public BiFunction<BitArray, Statement, String> getDisassembler() {
        return disassembler;
    }
}
