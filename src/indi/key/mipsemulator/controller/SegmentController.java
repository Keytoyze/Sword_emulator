package indi.key.mipsemulator.controller;

import java.awt.Canvas;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.util.TimingRenderer;

public class SegmentController implements TickCallback {

    private Machine machine;
    private Canvas canvas;
    private boolean isText = true, isHighPart = false;
    private BitArray value;

    public SegmentController(Canvas canvas, Machine machine) {
        this.machine = machine;
        TimingRenderer.register(this);
    }

    public void setValue(BitArray bitArray) {
        value = bitArray;
    }

    public void setText(boolean isText) {
        this.isText = isText;
    }

    public void setShowPart(boolean highPart) {
        this.isHighPart = highPart;
    }

    @Override
    public void onTick() {

    }
}
