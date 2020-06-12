package indi.key.mipsemulator.storage;

import java.util.function.Function;
import java.util.function.Supplier;

import indi.key.mipsemulator.controller.component.ButtonController;
import indi.key.mipsemulator.controller.component.KeyboardController;
import indi.key.mipsemulator.controller.component.SegmentController;
import indi.key.mipsemulator.util.FxUtils;
import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.SwordPrefs;
import indi.key.mipsemulator.vga.GraphProvider;
import indi.key.mipsemulator.vga.TextProvider;

public enum MemoryType {

    RAM(null, SwordPrefs.RAM, () -> {
        try {
            return Integer.parseInt(SwordPrefs.RAM_SIZE.get().substring(2), 16);
        } catch (NumberFormatException e) {
            FxUtils.showException(e);
            return 0x400000;
        }
    }),
    VRAM_TEXT(TextProvider.TextVram::new, SwordPrefs.VRAM_TEXT, () -> 80 * 60 * 8),
    VRAM_GRAPH(GraphProvider.GraphVram::new, SwordPrefs.VRAM_GRAPH, () -> 640 * 480 * 2),
    SEGMENT(SegmentController.SegmentMemory::new,
            SwordPrefs.SEGMENT, () -> 0x100),
    GPIO(GpioRegister::new, SwordPrefs.GPIO),
    BUTTON(ButtonController.ButtonMemory::new, SwordPrefs.BUTTON),
    COUNTER(null, SwordPrefs.COUNTER),
    PS2(KeyboardController.PS2Memory::new, SwordPrefs.PS2);

    private Function<Integer, Memory> memorySupplier;
    private SwordPrefs beginPref;
    private long[] addresses;
    public static MemoryType[] VALUE = values();
    private int capacity;

    MemoryType(Function<Integer, Memory> memorySupplier, SwordPrefs prefs) {
        this(memorySupplier, prefs, () -> 4);
    }

    MemoryType(Function<Integer, Memory> memorySupplier, SwordPrefs prefs, Supplier<Integer> capacitySupplier) {
        this.beginPref = prefs;
        this.capacity = capacitySupplier.get();
        this.addresses = IoUtils.stringToLong(prefs.get());
        if (memorySupplier == null) {
            this.memorySupplier = capacity == 4 ? integer -> new RegisterMemory() : ByteArrayMemory::new;
        } else {
            this.memorySupplier = memorySupplier;
        }
    }

    public int getRelativeAddress(long address, int length) {
        for (long a : addresses) {
            if (address >= a && a + this.capacity >= address + length) {
                return (int) (address - a);
            }
        }
        return -1;
    }

    public Memory generateStorage() {
        return memorySupplier.apply(getCapacity());
    }

    public int getCapacity() {
        return capacity;
    }

    public SwordPrefs getPref() {
        return beginPref;
    }

}
