package indi.key.mipsemulator.core.controller;

import indi.key.mipsemulator.model.interfaces.Resetable;
import indi.key.mipsemulator.storage.AddressRedirector;

public class Machine implements Resetable {

    private Cpu cpu;
    private AddressRedirector addressRedirector;


    @Override
    public void reset() {

    }
}
