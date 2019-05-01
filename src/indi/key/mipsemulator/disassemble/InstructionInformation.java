/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package indi.key.mipsemulator.disassemble;

public enum InstructionInformation {

    ADD(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    ADDI(OperandListPrototypes.SOURCE2_SOURCE_IMMEDIATE),
    ADDIU( OperandListPrototypes.SOURCE2_SOURCE_IMMEDIATE),
    ADDU( OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    AND(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    ANDI(OperandListPrototypes.SOURCE2_SOURCE_IMMEDIATE),
    BEQ(OperandListPrototypes.SOURCE_SOURCE2_OFFSET),
    BEQL(OperandListPrototypes.SOURCE_SOURCE2_OFFSET),
    BGEZ(OperandListPrototypes.SOURCE_OFFSET),
    BGEZAL(OperandListPrototypes.SOURCE_OFFSET),
    BGEZALL(OperandListPrototypes.SOURCE_OFFSET),
    BGEZL(OperandListPrototypes.SOURCE_OFFSET),
    BGTZ(OperandListPrototypes.SOURCE_OFFSET),
    BGTZL(OperandListPrototypes.SOURCE_OFFSET),
    BLEZ(OperandListPrototypes.SOURCE_OFFSET),
    BLEZL(OperandListPrototypes.SOURCE_OFFSET),
    BLTZ(OperandListPrototypes.SOURCE_OFFSET),
    BLTZAL(OperandListPrototypes.SOURCE_OFFSET),
    BLTZALL(OperandListPrototypes.SOURCE_OFFSET),
    BLTZL(OperandListPrototypes.SOURCE_OFFSET),
    BNE(OperandListPrototypes.SOURCE_SOURCE2_OFFSET),
    BNEL(OperandListPrototypes.SOURCE_SOURCE2_OFFSET),
    BREAK(OperandListPrototypes.EMPTY),
    COP2(OperandListPrototypes.COPROCESSOR_FUNCTION),
    DIV(OperandListPrototypes.SOURCE_SOURCE2),
    DIVU(OperandListPrototypes.SOURCE_SOURCE2),
    DERET(OperandListPrototypes.EMPTY),
    ERET(OperandListPrototypes.EMPTY),
    J(OperandListPrototypes.TARGET),
    JAL(OperandListPrototypes.TARGET),
    // Destination 31 can be implied, but it is not implemented here.
    JALR(OperandListPrototypes.DESTINATION_SOURCE),
    JR(OperandListPrototypes.SOURCE),
    LB(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LBU(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LDC1(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LDC2(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LH(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LHU(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LL(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LUI(OperandListPrototypes.SOURCE2_IMMEDIATE),
    LW(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LWC1(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LWC2(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LWL(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    LWR(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    MFC0(OperandListPrototypes.SOURCE2_DESTINATION),
    MFHI(OperandListPrototypes.DESTINATION),
    MFLO(OperandListPrototypes.DESTINATION),
    MOVE(OperandListPrototypes.DESTINATION_SOURCE),
    MOVN(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    MOVZ(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    MTC0(OperandListPrototypes.SOURCE2_DESTINATION),
    MTHI(OperandListPrototypes.SOURCE),
    MTLO(OperandListPrototypes.SOURCE),
    MULT(OperandListPrototypes.SOURCE_SOURCE2),
    MULTU(OperandListPrototypes.SOURCE_SOURCE2),
    NOP(OperandListPrototypes.EMPTY),
    NOR(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    OR(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    ORI(OperandListPrototypes.SOURCE2_SOURCE_IMMEDIATE),
    PREF(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SB(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SC(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SDC1(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SDC2(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SH(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SLL(OperandListPrototypes.DESTINATION_SOURCE2_SHIFT_AMOUNT),
    SLLV(OperandListPrototypes.DESTINATION_SOURCE2_SOURCE),
    SLT(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    SLTI(OperandListPrototypes.SOURCE2_SOURCE_IMMEDIATE),
    SLTIU(OperandListPrototypes.SOURCE2_SOURCE_IMMEDIATE),
    SLTU(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    SRA(OperandListPrototypes.DESTINATION_SOURCE2_SHIFT_AMOUNT),
    SRAV(OperandListPrototypes.DESTINATION_SOURCE2_SOURCE),
    SRL(OperandListPrototypes.DESTINATION_SOURCE2_SHIFT_AMOUNT),
    SRLV(OperandListPrototypes.DESTINATION_SOURCE2_SOURCE),
    SUB(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    SUBU(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    SW(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SWC1(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SWC2(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SWC3(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SWL(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SWR(OperandListPrototypes.SOURCE2_OFFSET_BASE),
    SYSCALL(OperandListPrototypes.EMPTY),
    TLBP(OperandListPrototypes.EMPTY),
    TLBR(OperandListPrototypes.EMPTY),
    TLBWI(OperandListPrototypes.EMPTY),
    TLBWR(OperandListPrototypes.EMPTY),
    WAIT(OperandListPrototypes.EMPTY),
    XOR(OperandListPrototypes.DESTINATION_SOURCE_SOURCE2),
    XORI(OperandListPrototypes.SOURCE2_SOURCE_IMMEDIATE);

    private OperandPrototype[] operandListPrototype;

    InstructionInformation(OperandPrototype[] operandListPrototype) {
        this.operandListPrototype = operandListPrototype;
    }

    public OperandPrototype[] getOperandListPrototype() {
        return operandListPrototype;
    }
    
}
