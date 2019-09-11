package indi.key.mipsemulator.core.controller;

import java.io.File;

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

    public byte[] loadMemory(long address, int bytesNum) {
        return addressRedirector.load(address, bytesNum);
    }

    public int loadInt(long address) {
        return addressRedirector.loadInt(address);
    }

    public Statement loadStatement(long address) {
        Memory source = rom == null ? addressRedirector : rom;
        return Statement.of(source.loadInt(address));
    }

    public void saveMemory(long address, byte[] data) {
        addressRedirector.save(address, data);
    }

    public void saveInt(long address, int data) {
        addressRedirector.saveInt(address, data);
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

}
