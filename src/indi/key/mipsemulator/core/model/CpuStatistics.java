package indi.key.mipsemulator.core.model;

import indi.key.mipsemulator.util.LogUtils;

public class CpuStatistics {
    public long time;
    public int instructionCount;
    public int errorCount;
    public Exception resentException;

    public CpuStatistics(long time, int instructionCount, int errorCount, Exception resentException) {
        this.time = time;
        this.instructionCount = instructionCount;
        this.errorCount = errorCount;
        this.resentException = resentException;
    }

    public void print() {
        LogUtils.m("=====Running Statistics=====");
        LogUtils.m("Duration: " + time + " ms");
        LogUtils.m("Instructions: " + instructionCount);
        LogUtils.m("MIPS (Million Instructions Per Second): " + instructionCount / 1000000.0 / (time / 1000.0));
        LogUtils.m("Error count: " + errorCount);
        if (resentException != null) {
            LogUtils.m("Resent Error: " + resentException.getMessage());
        }
    }

    @Override
    public String toString() {
        return "time: " + time + ", model: " + instructionCount + ", error: " + errorCount;
    }
}
