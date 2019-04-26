package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;

public class ChineseProvider extends ScreenProvider {

    public ChineseProvider(Machine machine) {
        super(machine);
    }

    @Override
    protected MemoryType getBindMemory() {
        return MemoryType.VRAM_TEXT;
    }

    @Override
    public void onMemoryChange(Memory memory, int address, int length) {

    }
}
