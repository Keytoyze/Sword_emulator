package indi.key.mipsemulator.vga;

import indi.key.mipsemulator.core.controller.Cpu;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;

public class ChineseProvider extends ScreenProvider {

    public ChineseProvider(Cpu cpu) {
        super(cpu);
    }

    @Override
    protected MemoryType getBindMemory() {
        return MemoryType.VRAM_TEXT;
    }

    @Override
    public void onMemoryChange(Memory memory, int address, int length) {

    }
}
