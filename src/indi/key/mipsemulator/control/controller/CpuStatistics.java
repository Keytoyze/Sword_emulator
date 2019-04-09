package indi.key.mipsemulator.control.controller;

public class CpuStatistics {
    public long time;
    public int instructionCount;
    public int errorCount;

    CpuStatistics(long time, int instructionCount, int errorCount) {
        this.time = time;
        this.instructionCount = instructionCount;
        this.errorCount = errorCount;
    }

    @Override
    public String toString() {
        return "time: " + time + ", model: " + instructionCount + ", error: " + errorCount;
    }
}
