package indi.key.mipsemulator.core.controller;

import java.io.File;

import indi.key.mipsemulator.controller.component.VgaController;
import indi.key.mipsemulator.core.model.CpuStatistics;
import indi.key.mipsemulator.core.model.Statement;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.interfaces.CpuCallback;
import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.storage.AddressRedirector;
import indi.key.mipsemulator.storage.ByteArrayMemory;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import indi.key.mipsemulator.util.LogUtils;
import indi.key.mipsemulator.vga.TextProvider;
import indi.key.mipsemulator.vga.VgaConfigures;

public class Machine implements Resetable {

    private static Machine instance;

    private Cpu cpu;
    private Register[] registers;
    private AddressRedirector addressRedirector;
    private ByteArrayMemory rom = null;
    private Counter counter;
    private BitArray switches;
    private BitArray buttons;
    private BitArray led;
    private Register hi = new Register(RegisterType.HI);
    private Register lo = new Register(RegisterType.LO);
    // for cursor
    private int cursorIndex = -1;
    private int cursorX = -1;
    private int cursorY = -1;
    private boolean blink = false;

    public static Machine getInstance() {
        if (instance == null) {
            instance = new Machine();
        }
        return instance;
    }

    private Machine() {
        this.addressRedirector = new AddressRedirector();
        registers = new Register[RegisterType.values().length];
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register(RegisterType.of(i));
        }
        counter = new Counter(this);
        cpu = new Cpu(this);
        reset();
    }

    public void setRamFile(File ram) {
        addressRedirector.setInitFile(ram);
    }

    public File getRamFile() {
        return addressRedirector.getInitFile();
    }

    public void setRomFile(File romFile) {
        if (romFile != null) {
            this.rom = new ByteArrayMemory(MemoryType.RAM.getCapacity());
            this.rom.setInitFile(romFile);
        } else {
            this.rom = null;
        }
    }

    public File getRomFile() {
        if (this.rom != null) {
            return this.rom.getInitFile();
        }
        return null;
    }

    public Memory getRom() {
        return rom;
    }

    public void setDelaySlot(boolean enable) {
        cpu.delaySlotEnable = enable;
    }

    public boolean isDelaySlotEnable() {
        return cpu.delaySlotEnable;
    }

    public void singleStep() {
        if (!cpu.isLooping()) {
            if (addressRedirector.hasInit()) {
                cpu.singleStep();
                ticks();
            } else {
                LogUtils.m("Machine hasn't loaded any initializing file.");
            }
        }
    }

    public void singleStepWithoutJal() {
        if (!cpu.isLooping()) {
            if (addressRedirector.hasInit()) {
                cpu.singleStepWithoutJal();
                ticks();
            } else {
                LogUtils.m("Machine hasn't loaded any initializing file.");
            }
        }
    }

    public boolean isLooping() {
        return cpu.isLooping();
    }

    public boolean loop() {
        if (addressRedirector.hasInit()) {
            counter.beginTicking();
            cpu.loop();
            return true;
        } else {
            LogUtils.m("Machine hasn't loaded any initializing file.");
            return false;
        }
    }

    public CpuStatistics exitLoop() {
        counter.endTicking();
        return cpu.exitLoop();
    }

    @Override
    public void reset() {
        if (cpu.isLooping()) {
            cpu.exitLoop();
        }
        for (Register register : registers) {
            register.reset();
        }
        addressRedirector.reset();
        if (rom != null) {
            rom.reset();
        }
        counter.endTicking();
        cpu.reset();
        hi.reset();
        lo.reset();
    }

    public Register getHi() {
        return hi;
    }

    public Register getLo() {
        return lo;
    }

    public Register getRegister(RegisterType registerType) {
        return registers[registerType.ordinal()];
    }

    public Register getRegister(BitArray bitArray) {
        return getRegister(RegisterType.of(bitArray));
    }

    public void addCpuListener(CpuCallback callback) {
        cpu.addCpuListener(callback);
    }

    public Cpu getCpu() {
        return cpu;
    }

    public Counter getCounter() {
        return counter;
    }

    public AddressRedirector getAddressRedirector() {
        return addressRedirector;
    }

    public Statement loadStatement(long address) {
        Memory source = rom == null ? addressRedirector : rom;
        return Statement.of(source.loadWord(address));
    }

    public void ticks() {
        counter.ticks();
    }

    public BitArray getSwitches() {
        return switches;
    }

    public void setButtons(BitArray bitArray) {
        this.buttons = bitArray;
    }

    public BitArray getButtons() {
        return buttons;
    }

    public void setLed(BitArray bitArray) {
        this.led = bitArray;
    }

    public BitArray getLed() {
        if (led == null) {
            return BitArray.ofLength(16);
        }
        return led;
    }

    public void setSwitches(BitArray bitArray) {
        this.switches = bitArray;
    }

    public void setCursor(BitArray cursorX, BitArray cursorY) {
        if (this.cursorX != cursorX.value() || this.cursorY != cursorY.value()) {
            this.cursorX = cursorX.value();
            this.cursorY = cursorY.value();
            updateCursor();
        }
    }

    public void blink() {
        blink = !blink;
        updateCursor();
    }

    public boolean shouldReverseCursor(int index) {
        if (!blink) {
            return false;
        }
        return index == this.cursorIndex;
    }

    private void updateCursor() {
        if (this.cursorX < 0 || this.cursorY < 0) {
            return;
        }
        int lineNum = VgaController.WIDTH / VgaConfigures.getFont().getWidth();
        int beforeCursorIndex = this.cursorIndex;
        this.cursorIndex = lineNum * this.cursorY + this.cursorX;
        TextProvider.TextVram textVram = (TextProvider.TextVram) addressRedirector.getMemory(MemoryType.VRAM_TEXT);
        textVram.redrawCursor(beforeCursorIndex, this.cursorIndex);
    }
}
