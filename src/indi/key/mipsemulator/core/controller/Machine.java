package indi.key.mipsemulator.core.controller;

import java.io.File;

import indi.key.mipsemulator.core.model.CpuStatistics;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.interfaces.CpuCallback;
import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.storage.AddressRedirector;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import indi.key.mipsemulator.util.LogUtils;

public class Machine implements Resetable {

    private static Machine instance;

    private Cpu cpu;
    private Register[] registers;
    private AddressRedirector addressRedirector;
    private Counter counter;
    private BitArray switches;
    private BitArray buttons;
    private BitArray led;

    public static Machine getInstance(File initFile) {
        if (instance == null) {
            instance = new Machine(initFile);
        } else {
            instance.setInitFile(initFile);
        }
        return instance;
    }

    public static Machine getReference() {
        return instance;
    }

    private Machine(File initFile) {
        this.addressRedirector = new AddressRedirector(initFile);
        registers = new Register[RegisterType.values().length];
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register(RegisterType.of(i));
        }
        counter = new Counter(this);
        cpu = new Cpu(this);
        reset();
    }

    private void setInitFile(File initFile) {
        this.addressRedirector.setInitFile(initFile);
        reset();
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
        counter.endTicking();
        cpu.reset();
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
