package indi.key.mipsemulator.core.controller;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import indi.key.mipsemulator.core.model.CpuStatistics;
import indi.key.mipsemulator.model.interfaces.RegisterListener;
import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.AddressRedirector;
import indi.key.mipsemulator.storage.Register;
import indi.key.mipsemulator.storage.RegisterType;
import indi.key.mipsemulator.util.TimingRenderer;
import javafx.application.Platform;

public class Machine implements Resetable, TickCallback {

    private Cpu cpu;
    private Register[] registers;
    private AddressRedirector addressRedirector;
    private Counter counter;

    private Set<RegisterListener> registerListenerSet = new HashSet<>();

    public Machine(File initFile) {
        this.addressRedirector = new AddressRedirector(initFile);
        registers = new Register[RegisterType.values().length];
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register(RegisterType.of(i));
        }
        counter = new Counter(this);
        cpu = new Cpu(this);
        reset();
    }

    public void singleStep() {
        if (!cpu.isLooping()) {
            cpu.singleStep();
            ticks();
        }
    }

    public void loop() {
        TimingRenderer.register(this);
        cpu.loop();
    }

    public CpuStatistics exitLoop() {
        TimingRenderer.unRegister(this);
        return cpu.exitLoop();
    }

    @Override
    public void reset() {
        for (Register register : registers) {
            register.reset();
        }
        addressRedirector.reset();
        counter.endTicking();
        cpu.reset();
    }

    public Register getHI() {
        return registers[RegisterType.HI.ordinal()];
    }

    public Register getLO() {
        return registers[RegisterType.LO.ordinal()];
    }


    public Register getRegister(RegisterType registerType) {
        return registers[registerType.ordinal()];
    }

    public void addRegisterListener(RegisterListener registerListener) {
        registerListenerSet.add(registerListener);
    }

    public void addCpuListener(TickCallback tickCallback) {
        cpu.addCpuListener(tickCallback);
    }

    public Cpu getCpu() {
        return cpu;
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

    @Override
    public void onTick() {
        Platform.runLater(() -> {
            for (RegisterListener listener : registerListenerSet) {
                for (Register register : registers) {
                    listener.onRegisterChange(register);
                }
            }
        });
    }


}
