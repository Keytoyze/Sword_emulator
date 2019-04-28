package indi.key.mipsemulator.core.model;

import indi.key.mipsemulator.core.action.Action;
import indi.key.mipsemulator.core.action.BranchAction;
import indi.key.mipsemulator.core.action.ITypeAction;
import indi.key.mipsemulator.core.action.JumpAction;
import indi.key.mipsemulator.core.action.MemoryAction;
import indi.key.mipsemulator.core.action.RTypeAction;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.exception.OverflowException;
import indi.key.mipsemulator.util.IoUtils;

@SuppressWarnings("unused")
public enum Instruction {
    ADD((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(checkOverflow(rs.getAsLong() + rt.getAsLong()));
    }),
    ADDI((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(checkOverflow(rs.getAsLong() + immediate.integerValue()));
    }),
    ADDIU((ITypeAction) (m, rs, rt, immediate) -> {
        rt.setUnsigned(rs.getUnsigned() + immediate.integerValue());
    }),
    ADDU((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.setUnsigned(rs.getUnsigned() + rt.getUnsigned());
    }),
    AND((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rs.get() & rt.get());
    }),
    ANDI((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(rs.get() & immediate.value());
    }),
    B,
    // BAL, BC1F, BC1FL, ... , BC2TL
    BEQ((BranchAction) (m, rs, rt) -> rs.equals(rt)),
    BEQL,
    BGEZ((BranchAction) (m, rs, rt) -> rs.get() >= 0),
    BGEZAL((BranchAction) (m, rs, rt) -> rs.get() >= 0, true),
    BGEZALL,
    BGEZL,
    BGTZ((BranchAction) (m, rs, rt) -> rs.get() > 0),
    BGTZL,
    BLEZ((BranchAction) (m, rs, rt) -> rs.get() <= 0),
    BLEZL,
    BLTZ((BranchAction) (m, rs, rt) -> rs.get() < 0),
    BLTZAL((BranchAction) (m, rs, rt) -> rs.get() < 0, true),
    BLTZALL,
    BLTZL,
    BNE((BranchAction) (m, rs, rt) -> !rs.equals(rt)),
    BNEL,
    BREAK,
    // C, ... , CLZ
    COP2,
    // CTC, ... , DERET
    DERET,
    DIV,
    DIVU,
    ERET,
    J((JumpAction) (m, statement) -> statement.getAddress() << 2),
    JAL((JumpAction) (m, statement) -> statement.getAddress() << 2, true),
    JALR((JumpAction) (m, statement) -> m.getRegister(statement.getRs()).get(), true),
    JR((JumpAction) (m, statement) -> m.getRegister(statement.getRs()).get()),
    LA,
    LB((MemoryAction) (m, address, rt) -> {
        rt.set(IoUtils.bytesToInt(m.loadMemory(address, 1)));
    }),
    LBU((MemoryAction) (m, address, rt) -> {
        rt.set(IoUtils.bytesToUnsignedInt(m.loadMemory(address, 1)));
    }),
    LDC1,
    LDC2,
    LH((MemoryAction) (m, address, rt) -> {
        rt.set(IoUtils.bytesToInt(m.loadMemory(address, 2)));
    }),
    LHU((MemoryAction) (m, address, rt) -> {
        rt.set(IoUtils.bytesToUnsignedInt(m.loadMemory(address, 2)));
    }),
    LI,
    LL,
    LUI((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(immediate.value() << 16);
    }),
    LW((MemoryAction) (m, address, rt) -> {
        rt.set(m.loadInt(address));
    }),
    LWC1,
    LWC2,
    //    LWC3,
    LWL,
    LWR,
    // MADD, ... MFC2
    MFC0,
    MFHI,
    MFLO,
    MOVE,
    MOVN,
    // MOVT
    MOVZ,
    // MSUB, ... ,MTC2
    MTC0,
    MTHI,
    MTLO,
    // MUL
    MULT,
    MULTU,
    NOR((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(~(rs.get() | rd.get()));
    }),
    NOP(),
    OR((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rs.get() | rt.get());
    }),
    ORI((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(rs.get() | immediate.value());
    }),
    PREF,
    SB((MemoryAction) (m, address, rt) -> {
        m.saveMemory(address, BitArray.ofValue(rt.get()).setLength(4).bytes());
    }),
    SC,
    // SDBBP
    SDC1,
    SDC2,
    //    SDL,
//    SDR,
    SH((MemoryAction) (m, address, rt) -> {
        m.saveMemory(address, BitArray.ofValue(rt.get()).setLength(8).bytes());
    }),
    SLL((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rt.get() << shamt);
    }),
    SLLV((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rt.get() << rs.get());
    }),
    SLT((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rs.get() < rt.get() ? 1 : 0);
    }),
    SLTI((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(rs.get() < immediate.integerValue() ? 1 : 0);
    }),
    SLTIU((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(rs.getUnsigned() < immediate.integerValue() ? 1 : 0);
    }),
    SLTU((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rs.getUnsigned() < rt.getUnsigned() ? 1 : 0);
    }),
    SRA((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rt.get() >> shamt);
    }),
    SRAV((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rt.get() >> rs.get());
    }),
    SRL((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rt.get() >>> shamt);
    }),
    SRLV((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rt.get() >>> rs.get());
    }),
    // SSNOP
    SUB((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(checkOverflow(rs.getAsLong() - rt.getAsLong()));
    }),
    SUBU((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.setUnsigned(rs.getUnsigned() - rt.getUnsigned());
    }),
    SW((MemoryAction) (m, address, rt) -> {
        m.saveInt(address, rt.get());
    }),
    SWC1,
    SWC2,
    SWC3,
    SWL,
    SWR,
    TLBP,
    TLBR,
    TLBWI,
    TLBWR,
    // TEQ, ... , TNEI
    SYSCALL,
    WAIT,
    XOR((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rs.get() ^ rt.get());
    }),
    XORI((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(rt.get() ^ immediate.value());
    }),
    UNKNOWN;

    private Action action;
    private boolean linkNext;

    Instruction() {
        this(null, false);
    }

    Instruction(Action action) {
        this(action, false);
    }

    Instruction(Action action, boolean linkNext) {
        this.action = action;
        this.linkNext = linkNext;
    }

    public Action getAction() {
        return action;
    }

    public boolean isLinkNext() {
        return linkNext;
    }

    private static int checkOverflow(long value) throws OverflowException {
        if ((int) value != value) {
            //throw new OverflowException("Result " + value + " overflow.");
        }
        return (int) value;
    }


    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
