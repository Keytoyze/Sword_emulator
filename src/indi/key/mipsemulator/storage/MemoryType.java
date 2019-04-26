package indi.key.mipsemulator.storage;

import java.util.function.Function;

import indi.key.mipsemulator.controller.ButtonController;
import indi.key.mipsemulator.controller.KeyboardController;
import indi.key.mipsemulator.model.info.Range;

public enum MemoryType {

    RAM(new Range<>(0x00000000L, 0x0000FFFFL)),
    VRAM_TEXT(new Range<>(0x00000000L, 0x000012BFL)),
    VRAM_GRAPH(new Range<>(0x00002000L, 0x00097FFFL)),
    SEGMENT(0xFFFFFE00L),
    SEGMENT_COMPAT(0xE0000000L),
    GPIO(0xFFFFFF00L, GpioRegister::new),
    GPIO_COMPAT(0xF0000000L),
    BUTTON(0xFFFFFC00L, ButtonController.ButtonMemory::new),
    BUTTON_COMPAT(0xC0000000L),
    COUNTER(0xFFFFFF04L),
    COUNTER_COMPAT(0xF0000004L),
    PS2(0xFFFFD000L, KeyboardController.PS2Memory::new);

    private Range<Long> addressRange;
    private Function<Integer, Memory> memorySupplier;

    MemoryType(Long address) {
        this(address, null);
    }

    MemoryType(Long address, Function<Integer, Memory> memorySupplier) {
        this(new Range<>(address, address + 3), memorySupplier);
    }

    MemoryType(Range<Long> addressRange) {
        this(addressRange, null);
    }

    MemoryType(Range<Long> addressRange, Function<Integer, Memory> memorySupplier) {
        this.addressRange = addressRange;
        if (memorySupplier == null) {
            this.memorySupplier = ByteArrayMemory::new;
        } else {
            this.memorySupplier = memorySupplier;
        }
    }

    public boolean contains(Range<Long> dataRange) {
        return contains(dataRange, 0);
    }

    public Memory generateStorage() {
        return memorySupplier.apply(getLength());
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
