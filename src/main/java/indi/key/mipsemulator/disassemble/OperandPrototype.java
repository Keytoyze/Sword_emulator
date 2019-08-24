/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package indi.key.mipsemulator.disassemble;

import java.util.function.Function;

import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.info.BitArray;

public class OperandPrototype {

    private Function<Statement, BitArray> bitArrayFunction;
    private OperandType type;

    private OperandPrototype(Function<Statement, BitArray> bitArrayFunction, OperandType type) {
        this.bitArrayFunction = bitArrayFunction;
        this.type = type;
    }

    public static OperandPrototype of(Function<Statement, BitArray> bitArrayFunction, OperandType type) {
        return new OperandPrototype(bitArrayFunction, type);
    }

    public Function<Statement, BitArray> getBitarrayFunction() {
        return bitArrayFunction;
    }

    public OperandType getType() {
        return type;
    }
}
