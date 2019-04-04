package indi.key.mipsemulator;

import indi.key.mipsemulator.controller.emulator.Cpu;
import indi.key.mipsemulator.model.storage.Ram;
import indi.key.mipsemulator.model.storage.Rom;

public class Main {

    public static void main(String[] args) {
        Ram ram = new Ram(65536);
        Rom rom = new Rom(65536);
        Cpu cpu = new Cpu(ram, rom);
        for (int i = 0; i < 50; i ++) {
            try {
                cpu.execute();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }
    }
}
