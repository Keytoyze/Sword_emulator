package indi.key.mipsemulator.storage;

import java.util.function.Function;

import indi.key.mipsemulator.controller.ButtonController;
import indi.key.mipsemulator.controller.KeyboardController;
import indi.key.mipsemulator.controller.SegmentController;
import indi.key.mipsemulator.model.info.Range;

// TODO: use common properties to control addresses
public enum MemoryType {

    RAM(new MemoryRange(0x00000000L, 0x0000FFFFL)),
    VRAM_TEXT(new MemoryRange(0x00000000L, 0x000012BFL)),
    VRAM_GRAPH(new MemoryRange(0x00002000L, 0x00097FFFL)),
    SEGMENT(SegmentController.SegmentMemory::new,
            new MemoryRange(0xFFFFFE00L, 0xFFFFFEFFL),
            new MemoryRange(0xE0000000L, 0xE00000FFL)),
    GPIO(GpioRegister::new,
            new MemoryRange(0xFFFFFF00L),
            new MemoryRange(0xF0000000L)),
    BUTTON(ButtonController.ButtonMemory::new,
            new MemoryRange(0xFFFFFC00L),
            new MemoryRange(0xC0000000L)),
    COUNTER(new MemoryRange(0xFFFFFF04L),
            new MemoryRange(0xF0000004L)),
    PS2(KeyboardController.PS2Memory::new,
            new MemoryRange(0xFFFFD000L));

    private Function<Integer, Memory> memorySupplier;
    private MemoryRange[] ranges;

    MemoryType(MemoryRange... address) {
        this(null, address);
    }

    MemoryType(Function<Integer, Memory> memorySupplier, MemoryRange... address) {
        this.ranges = address;
        if (memorySupplier == null) {
            this.memorySupplier = ByteArrayMemory::new;
        } else {
            this.memorySupplier = memorySupplier;
        }
    }

    public int getRelativeAddress(Range<Long> dataRange) {
        return getRelativeAddress(dataRange, 0);
    }

    public int getRelativeAddress(Range<Long> dataRange, int offset) {
        Range<Long> realDataRange = new Range<>(dataRange);
        realDataRange.setStart(realDataRange.getStart() - offset);
        realDataRange.setEnd(realDataRange.getEnd() - offset);
        for (MemoryRange range : ranges) {
            if (range.contains(realDataRange)) {
                return range.getRelativeAddress(realDataRange.getStart());
            }
        }
        return -1;
    }

    public Memory generateStorage() {
        return memorySupplier.apply(getLength());
    }

//    public int getRelativeAddress(long address) {
//        for (MemoryRange range : ranges) {
//            if (range.contains(dataRange, offset)) {
//                return range.contains(dataRange, offset);
//            }
//        }
//        return -1;
//    }

    public int getLength() {
        return ranges[0].getLength();
    }

    private static class MemoryRange {

        Range<Long> addressRange;

        MemoryRange(long address) {
            this(address, address + 3);
        }

        MemoryRange(long begin, long end) {
            this.addressRange = new Range<>(begin, end);
        }

        boolean contains(Range<Long> dataRange) {
            Range<Long> realDataRange = new Range<>(dataRange);
            realDataRange.setStart(realDataRange.getStart());
            realDataRange.setEnd(realDataRange.getEnd());
            return addressRange.contains(realDataRange);
        }

        int getLength() {
            return (int) (addressRange.getEnd() - addressRange.getStart() + 1);
        }

        int getRelativeAddress(long address) {
            return (int) (address - addressRange.getStart());
        }
    }
}
