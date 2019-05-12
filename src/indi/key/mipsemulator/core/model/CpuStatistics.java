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
        StringBuilder sb = new StringBuilder("=====Running Statistics=====").append("\n");
        sb.append("Duration: ").append(time).append(" ms\n");
        sb.append("Instructions: ").append(instructionCount).append("\n");
        sb.append("MIPS (Million Instructions Per Second): ").append(instructionCount / 1000000.0 / (time / 1000.0))
                .append("\n");
        sb.append("Error count: ").append(errorCount).append("\n");
        if (resentException != null) {
            sb.append("Resent Error: ").append(resentException.getMessage());
        }
        LogUtils.m(sb.toString());
    }

    @Override
    public String toString() {
        return "time: " + time + ", model: " + instructionCount + ", error: " + errorCount;
    }
}
