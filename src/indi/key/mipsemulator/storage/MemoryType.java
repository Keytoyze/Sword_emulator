package indi.key.mipsemulator.storage;

import java.util.function.Function;

import indi.key.mipsemulator.controller.component.ButtonController;
import indi.key.mipsemulator.controller.component.SegmentController;
import indi.key.mipsemulator.model.info.Range;
import indi.key.mipsemulator.util.SwordPrefs;

public enum MemoryType {

    RAM(null, SwordPrefs.RAM, 0x10000),
    VRAM(null, SwordPrefs.VRAM, 0x99000),
    SEGMENT(SegmentController.SegmentMemory::new,
            SwordPrefs.SEGMENT, 0x100),
    GPIO(GpioRegister::new, SwordPrefs.GPIO),
    BUTTON(ButtonController.ButtonMemory::new, SwordPrefs.BUTTON),
    COUNTER(null, SwordPrefs.COUNTER),
    PS2(null, SwordPrefs.PS2);

    private Function<Integer, Memory> memorySupplier;
    private SwordPrefs beginPref;
    private int length;

    MemoryType(Function<Integer, Memory> memorySupplier, SwordPrefs prefs) {
        this(memorySupplier, prefs, 4);
    }

    MemoryType(Function<Integer, Memory> memorySupplier, SwordPrefs prefs, int length) {
        this.beginPref = prefs;
        this.length = length;
        if (memorySupplier == null) {
            this.memorySupplier = ByteArrayMemory::new;
        } else {
            this.memorySupplier = memorySupplier;
        }
    }

    public int getRelativeAddress(Range<Long> dataRange) {
        return (int) (dataRange.getStart() - beginPref.get());
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
        return length;
    }

    public void setAddressBeginOffset(long offset) {
        beginPref.set(offset);
    }

}
