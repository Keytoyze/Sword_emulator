package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.model.Range;

public enum MemoryType {

    RAM(new Range<>(0x00000000L, 0x0000FFFFL)),
    VRAM_TEXT(new Range<>(0x00000000L, 0x000012BFL)),
    VRAM_GRAPH(new Range<>(0x00002000L, 0x0004CFFFL)),
    SEGMENT(0xFFFFFE00L),
    SEGMENT_COMPAT(0xE0000000L),
    GPIO(0xFFFFFF00L),
    GPIO_COMPAT(0xF0000000L),
    BUTTON(0xFFFFFC00L),
    BUTTON_COMPAT(0xC0000000L),
    COUNTER(0xFFFFFF04L),
    COUNTER_COMPAT(0xF0000004L),
    PS2(0xFFFFD000L);


    private Range<Long> addressRange;

    MemoryType(Long address) {
        this.addressRange = new Range<>(address, address + 3);
    }

    MemoryType(Range<Long> addressRange) {
        this.addressRange = addressRange;
    }

    public boolean contains(Range<Long> dataRange) {
        return contains(dataRange, 0);
    }

    public IMemory generateStorage() {
        return new ByteArrayMemory(getLength());
    }

    public int getRelativeAddress(long address) {
        return (int) (address - addressRange.getStart());
    }

    public int getLength() {
        return (int) (addressRange.getEnd() - addressRange.getStart() + 1);
    }

    public boolean contains(Range<Long> dataRange, int offset) {
        Range<Long> realDataRange = new Range<>(dataRange);
        realDataRange.setStart(realDataRange.getStart() - offset);
        realDataRange.setEnd(realDataRange.getEnd() - offset);
        return addressRange.contains(realDataRange);
    }
}
