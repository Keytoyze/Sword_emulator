package indi.key.mipsemulator.storage;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.BitArray;

public class GpioRegister extends SplitRegisterMemory<RegisterMemory> {

    private BitArray ledData = BitArray.ofEmpty();

    public GpioRegister(int length) {
        super(new RegisterMemory(), new RegisterMemory());
    }

    @Override
    public void beforeLoad(RegisterMemory memory) {
        Machine machine = Machine.getInstance();
        BitArray bitArray = memory.getBitArray();
        boolean counterOut = machine.getCounter().getCounterOut();
        bitArray.set(31, counterOut);
        bitArray.set(30, counterOut);
        bitArray.set(29, counterOut);
        bitArray.setTo(16, machine.getButtons());
        bitArray.setTo(0, machine.getSwitches());
    }

    @Override
    public void onSave(RegisterMemory memory) {
        Machine machine = Machine.getInstance();
        memory.getBitArray().subArray(2, 18, ledData);
        machine.setLed(ledData);
    }
}
