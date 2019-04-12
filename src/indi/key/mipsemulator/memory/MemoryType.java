package indi.key.mipsemulator.memory;

import indi.key.mipsemulator.model.Range;

public enum MemoryType {

    RAM(new Range<>(0x00000000, 0x0000FFFF)),
    VRAM_TEXT(new Range<>(0x00000000, 0x000012BF)),
    VRAM_GRAPH(new Range<>(0x00002000, 0x0004CFFF)),
    SEGMENT(0xFFFFFE00),
    SEGMENT_COMPAT(0xE0000000),
    GPIO(0xFFFFFF00),
    GPIO_COMPAT(0xF0000000),
    BUTTON(0xFFFFFC00),
    BUTTON_COMPAT(0xC0000000),
    COUNTER(0xFFFFFF04),
    COUNTER_COMPAT(0xF0000004),
    PS2(0xFFFFD000);


    private Range<Integer> addressRange;

    MemoryType(Integer address) {
        this.addressRange = new Range<>(address, address + 3);
    }

    MemoryType(Range<Integer> addressRange) {
        this.addressRange = addressRange;
    }

    public boolean contains(Range<Integer> dataRange) {
        return contains(dataRange, 0);
    }

    public IMemory generateStorage() {
        return new ByteArrayMemory(getLength());
    }

    public int getRelativeAddress(int address) {
        return address - addressRange.getStart();
    }

    public int getLength() {
        return addressRange.getEnd() - addressRange.getStart();
    }

    public boolean contains(Range<Integer> dataRange, int offset) {
        Range<Integer> realDataRange = new Range<>(dataRange);
        realDataRange.setStart(realDataRange.getStart() - offset);
        realDataRange.setEnd(realDataRange.getEnd() - offset);
        return addressRange.contains(realDataRange);
    }
}
