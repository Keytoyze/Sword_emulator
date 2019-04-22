package indi.key.mipsemulator.core.model;

import indi.key.mipsemulator.core.action.Action;
import indi.key.mipsemulator.core.action.ConditionalAction;
import indi.key.mipsemulator.core.action.ITypeAction;
import indi.key.mipsemulator.core.action.JumpAction;
import indi.key.mipsemulator.core.action.MemoryAction;
import indi.key.mipsemulator.core.action.RTypeAction;
import indi.key.mipsemulator.model.bean.BitArray;
import indi.key.mipsemulator.model.exception.OverflowException;
import indi.key.mipsemulator.util.IoUtils;

@SuppressWarnings("unused")
public enum Instruction {
    ADD((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(checkOverflow(rs.getAsLong() + rt.getAsLong()));
    }),
    ADDI((ITypeAction) (cpu, rs, rt, immediate) -> {
        rt.set(checkOverflow(rs.getAsLong() + immediate.integerValue()));
    }),
    ADDIU((ITypeAction) (cpu, rs, rt, immediate) -> {
        rt.setUnsigned(rs.getUnsigned() + immediate.value());
    }),
    ADDU((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.setUnsigned(rs.getUnsigned() + rt.getUnsigned());
    }),
    AND((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rs.get() & rt.get());
    }),
    ANDI((ITypeAction) (cpu, rs, rt, immediate) -> {
        rt.set(rs.get() & immediate.value());
    }),
    B,
    // BAL, BC1F, BC1FL, ... , BC2TL
    BEQ((ConditionalAction) (cpu, rs, rt) -> rs.equals(rt)),
    BEQL,
    BGEZ((ConditionalAction) (cpu, rs, rt) -> rs.get() >= 0),
    BGEZAL((ConditionalAction) (cpu, rs, rt) -> rs.get() >= 0, true),
    BGEZALL,
    BGEZL,
    BGTZ((ConditionalAction) (cpu, rs, rt) -> rs.get() > 0),
    BGTZL,
    BLEZ((ConditionalAction) (cpu, rs, rt) -> rs.get() <= 0),
    BLEZL,
    BLTZ((ConditionalAction) (cpu, rs, rt) -> rs.get() < 0),
    BLTZAL((ConditionalAction) (cpu, rs, rt) -> rs.get() < 0, true),
    BLTZALL,
    BLTZL,
    BNE((ConditionalAction) (cpu, rs, rt) -> !rs.equals(rt)),
    BNEL,
    BREAK,
    // C, ... , CLZ
    COP2,
    // CTC, ... , DERET
    DERET,
    DIV,
    DIVU,
    ERET,
    J((JumpAction) (cpu, statement) -> statement.getAddress() << 2),
    JAL((JumpAction) (cpu, statement) -> statement.getAddress() << 2, true),
    JALR((JumpAction) (cpu, statement) -> cpu.getRegister(statement.getRs()).get(), true),
    JR((JumpAction) (cpu, statement) -> cpu.getRegister(statement.getRs()).get()),
    LA,
    LB((MemoryAction) (cpu, address, rt) -> {
        rt.set(IoUtils.bytesToInt(cpu.loadMemory(address, 1)));
    }),
    LBU((MemoryAction) (cpu, address, rt) -> {
        rt.set(IoUtils.bytesToUnsignedInt(cpu.loadMemory(address, 1)));
    }),
    LDC1,
    LDC2,
    LH((MemoryAction) (cpu, address, rt) -> {
        rt.set(IoUtils.bytesToInt(cpu.loadMemory(address, 2)));
    }),
    LHU((MemoryAction) (cpu, address, rt) -> {
        rt.set(IoUtils.bytesToUnsignedInt(cpu.loadMemory(address, 2)));
    }),
    LI,
    LL,
    LUI((ITypeAction) (cpu, rs, rt, immediate) -> {
        rt.set(immediate.value() << 16);
    }),
    LW((MemoryAction) (cpu, address, rt) -> {
        rt.set(cpu.loadInt(address));
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
    NOR((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(~(rs.get() | rd.get()));
    }),
    NOP(),
    OR((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rs.get() | rt.get());
    }),
    ORI((ITypeAction) (cpu, rs, rt, immediate) -> {
        rt.set(rs.get() | immediate.value());
    }),
    PREF,
    SB((MemoryAction) (cpu, address, rt) -> {
        cpu.saveMemory(address, BitArray.ofValue(rt.get()).setLength(4).bytes());
    }),
    SC,
    // SDBBP
    SDC1,
    SDC2,
    //    SDL,
//    SDR,
    SH((MemoryAction) (cpu, address, rt) -> {
        cpu.saveMemory(address, BitArray.ofValue(rt.get()).setLength(8).bytes());
    }),
    SLL((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rt.get() << shamt);
    }),
    SLLV((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rt.get() << rs.get());
    }),
    SLT((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rs.get() < rt.get() ? 1 : 0);
    }),
    SLTI((ITypeAction) (cpu, rs, rt, immediate) -> {
        rt.set(rs.get() < immediate.integerValue() ? 1 : 0);
    }),
    SLTIU((ITypeAction) (cpu, rs, rt, immediate) -> {
        rt.set(rs.getUnsigned() < immediate.value() ? 1 : 0);
    }),
    SLTU((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rs.getUnsigned() < rt.getUnsigned() ? 1 : 0);
    }),
    SRA((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rt.get() >> shamt);
    }),
    SRAV((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rt.get() >> rs.get());
    }),
    SRL((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rt.get() >>> shamt);
    }),
    SRLV((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rt.get() >>> rs.get());
    }),
    // SSNOP
    SUB((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(checkOverflow(rs.getAsLong() - rt.getAsLong()));
    }),
    SUBU((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.setUnsigned(rs.getUnsigned() - rt.getUnsigned());
    }),
    SW((MemoryAction) (cpu, address, rt) -> {
        cpu.saveInt(address, rt.get());
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
    XOR((RTypeAction) (cpu, rs, rt, rd, shamt) -> {
        rd.set(rs.get() ^ rt.get());
    }),
    XORI((ITypeAction) (cpu, rs, rt, immediate) -> {
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
