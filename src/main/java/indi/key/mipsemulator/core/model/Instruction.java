package indi.key.mipsemulator.core.model;

import indi.key.mipsemulator.core.action.Action;
import indi.key.mipsemulator.core.action.BranchAction;
import indi.key.mipsemulator.core.action.ITypeAction;
import indi.key.mipsemulator.core.action.JumpAction;
import indi.key.mipsemulator.core.action.MemoryAction;
import indi.key.mipsemulator.core.action.RTypeAction;
import indi.key.mipsemulator.model.exception.OverflowException;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.util.IoUtils;

// TODO: currently delay slots and exception handlers are not supported.
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
    // BAL, BC1F, BC1FL, ... , BC2TL
    BEQ((BranchAction) (m, rs, rt) -> rs.equals(rt), DelaySlotType.ALWAYS),
    BEQL((BranchAction) (m, rs, rt) -> rs.equals(rt), DelaySlotType.LIKELY),
    BGEZ((BranchAction) (m, rs, rt) -> rs.get() >= 0, DelaySlotType.ALWAYS),
    BGEZAL((BranchAction) (m, rs, rt) -> rs.get() >= 0, true, DelaySlotType.ALWAYS),
    BGEZALL((BranchAction) (m, rs, rt) -> rs.get() >= 0, true, DelaySlotType.LIKELY),
    BGEZL((BranchAction) (m, rs, rt) -> rs.get() >= 0, DelaySlotType.LIKELY),
    BGTZ((BranchAction) (m, rs, rt) -> rs.get() > 0, DelaySlotType.ALWAYS),
    BGTZL((BranchAction) (m, rs, rt) -> rs.get() > 0, DelaySlotType.LIKELY),
    BLEZ((BranchAction) (m, rs, rt) -> rs.get() <= 0, DelaySlotType.ALWAYS),
    BLEZL((BranchAction) (m, rs, rt) -> rs.get() <= 0, DelaySlotType.LIKELY),
    BLTZ((BranchAction) (m, rs, rt) -> rs.get() < 0, DelaySlotType.ALWAYS),
    BLTZAL((BranchAction) (m, rs, rt) -> rs.get() < 0, true, DelaySlotType.ALWAYS),
    BLTZALL((BranchAction) (m, rs, rt) -> rs.get() < 0, true, DelaySlotType.LIKELY),
    BLTZL((BranchAction) (m, rs, rt) -> rs.get() < 0, DelaySlotType.LIKELY),
    BNE((BranchAction) (m, rs, rt) -> !rs.equals(rt), DelaySlotType.ALWAYS),
    BNEL((BranchAction) (m, rs, rt) -> !rs.equals(rt), DelaySlotType.LIKELY),
    BREAK,
    // C, ... , CLZ
    COP2,
    // CTC, ... , DERET
    DERET,
    DIV((RTypeAction) (m, rs, rt, rd, shamt) -> {
        m.getLo().set(rs.get() / rt.get());
        m.getHi().set(rs.get() % rt.get());
    }),
    DIVU((RTypeAction) (m, rs, rt, rd, shamt) -> {
        m.getLo().set((int) (rs.getUnsigned() / rt.getUnsigned()));
        m.getHi().set((int) (rs.getUnsigned() % rt.getUnsigned()));
    }),
    ERET,
    J((JumpAction) (m, statement) -> statement.address.value() << 2, DelaySlotType.ALWAYS),
    JAL((JumpAction) (m, statement) -> statement.address.value() << 2, true, DelaySlotType.ALWAYS),
    JALR((JumpAction) (m, statement) -> statement.rsReg.get(), true, DelaySlotType.ALWAYS),
    JR((JumpAction) (m, statement) -> statement.rsReg.get(), DelaySlotType.ALWAYS),
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
    LL((MemoryAction) (m, address, rt) -> {
        rt.set(m.loadInt(address));
    }),
    LUI((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(immediate.value() << 16);
    }),
    LW(LL),
    LWC1,
    LWC2,
    //    LWC3,
    LWL,
    LWR,
    // MADD, ... MFC2
    MFC0,
    MFHI((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(m.getHi().get());
    }),
    MFLO((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(m.getLo().get());
    }),
    MOVE,
    MOVN,
    // MOVT
    MOVZ,
    // MSUB, ... ,MTC2
    MTC0,
    MTHI((RTypeAction) (m, rs, rt, rd, shamt) -> {
        m.getHi().set(rs.get());
    }),
    MTLO((RTypeAction) (m, rs, rt, rd, shamt) -> {
        m.getLo().set(rs.get());
    }),
    // MUL
    MULT((RTypeAction) (m, rs, rt, rd, shamt) -> {
        long result = rs.getAsLong() * rt.getAsLong();
        m.getLo().set((int) (result & 0xFFFFFFFFL));
        m.getHi().set((int) (result >> 32));
    }),
    MULTU((RTypeAction) (m, rs, rt, rd, shamt) -> {
        long result = rs.getUnsigned() * rt.getUnsigned();
        m.getLo().set((int) (result & 0xFFFFFFFFL));
        m.getHi().set((int) (result >> 32));
    }),
    NOR((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(~(rs.get() | rt.get()));
    }),
    NOP,
    OR((RTypeAction) (m, rs, rt, rd, shamt) -> {
        rd.set(rs.get() | rt.get());
    }),
    ORI((ITypeAction) (m, rs, rt, immediate) -> {
        rt.set(rs.get() | immediate.value());
    }),
    PREF,
    SB((MemoryAction) (m, address, rt) -> {
        m.saveMemory(address, BitArray.ofValue(rt.get()).setLength(8).bytes());
    }),
    SC((MemoryAction) (m, address, rt) -> {
        m.saveInt(address, rt.get());
    }),
    // SDBBP
    SDC1,
    SDC2,
    //    SDL,
//    SDR,
    SH((MemoryAction) (m, address, rt) -> {
        m.saveMemory(address, BitArray.ofValue(rt.get()).setLength(16).bytes());
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
    SW(SC),
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
    private int delaySlotType;

    Instruction() {
        this(null, false, DelaySlotType.DISABLE);
    }

    Instruction(Instruction delegate) {
        this(delegate.action, delegate.linkNext, delegate.delaySlotType);
    }

    Instruction(Action action) {
        this(action, false);
    }

    Instruction(Action action, boolean linkNext) {
        this(action, linkNext, DelaySlotType.DISABLE);
    }

    Instruction(Action action, int delaySlotType) {
        this(action, false, delaySlotType);
    }

    Instruction(Action action, boolean linkNext, int delaySlotType) {
        this.action = action;
        this.linkNext = linkNext;
        this.delaySlotType = delaySlotType;
    }

    public Action getAction() {
        return action;
    }

    public int getDelaySlotType() {
        return delaySlotType;
    }

    public boolean isLinkNext() {
        return linkNext;
    }

    private static int checkOverflow(long value) throws OverflowException {
        if ((int) value != value) {
            // remove due to so many programs use ADD just like ADDU
            //throw new OverflowException("Result " + value + " overflow.");
        }
        return (int) value;
    }


    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
