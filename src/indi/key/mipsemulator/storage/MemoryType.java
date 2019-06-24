package indi.key.mipsemulator.storage;

import java.util.function.Function;

import indi.key.mipsemulator.controller.component.ButtonController;
import indi.key.mipsemulator.controller.component.KeyboardController;
import indi.key.mipsemulator.controller.component.SegmentController;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.SwordPrefs;

public enum MemoryType {

    RAM(null, SwordPrefs.RAM, 0x10000),
    VRAM(null, SwordPrefs.VRAM, 0x99000),
    SEGMENT(SegmentController.SegmentMemory::new,
            SwordPrefs.SEGMENT, 0x100),
    GPIO(GpioRegister::new, SwordPrefs.GPIO),
    BUTTON(ButtonController.ButtonMemory::new, SwordPrefs.BUTTON),
    COUNTER(null, SwordPrefs.COUNTER),
    PS2(KeyboardController.PS2Memory::new, SwordPrefs.PS2);

    private Function<Integer, Memory> memorySupplier;
    private SwordPrefs beginPref;
    private Long[] addresses;
    private int length;

    MemoryType(Function<Integer, Memory> memorySupplier, SwordPrefs prefs) {
        this(memorySupplier, prefs, 4);
    }

    MemoryType(Function<Integer, Memory> memorySupplier, SwordPrefs prefs, int length) {
        this.beginPref = prefs;
        this.length = length;
        this.addresses = IoUtils.stringToLong(prefs.get());
        if (memorySupplier == null) {
            this.memorySupplier = ByteArrayMemory::new;
        } else {
            this.memorySupplier = memorySupplier;
        }
    }

    public int getRelativeAddress(Long address, int length) {
        for (Long a : addresses) {
            if (address >= a && a + this.length >= address + length) {
                return (int) (address - a);
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
        return length;
    }

    public SwordPrefs getPref() {
        return beginPref;
    }

}
