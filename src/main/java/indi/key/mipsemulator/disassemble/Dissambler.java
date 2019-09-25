package indi.key.mipsemulator.disassemble;

import java.util.function.BiFunction;
import java.util.function.Function;

import indi.key.mipsemulator.core.model.Operation;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.info.BitArray;

public class Dissambler {

    public static String dissambleStatement(Statement statement) {
        Operation operation = statement.operation;
        InstructionInformation information = operation.toInstructionInformation();
        OperandPrototype[] prototypes = information.getOperandListPrototype();
        StringBuilder stringBuilder = new StringBuilder(operation.toString());
        for (int i = 0; i < prototypes.length; i++) {
            OperandPrototype prototype = prototypes[i];
            OperandType type = prototype.getType();
            Function<Statement, BitArray> statementBitArrayFunction = prototype.getBitarrayFunction();
            BitArray elementBits = null;
            if (statementBitArrayFunction != null) {
                elementBits = statementBitArrayFunction.apply(statement);
            }
            BiFunction<BitArray, Statement, String> dissambler = type.getDisassembler();
            stringBuilder.append(" ").append(dissambler.apply(elementBits, statement));
            if (i != prototypes.length - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
}
