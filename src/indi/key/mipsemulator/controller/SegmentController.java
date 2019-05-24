package indi.key.mipsemulator.controller;

import indi.key.mipsemulator.core.controller.Machine;
import indi.key.mipsemulator.core.controller.TimingRenderer;
import indi.key.mipsemulator.model.exception.MemoryOutOfBoundsException;
import indi.key.mipsemulator.model.info.BitArray;
import indi.key.mipsemulator.model.interfaces.TickCallback;
import indi.key.mipsemulator.storage.ByteArrayMemory;
import indi.key.mipsemulator.storage.Memory;
import indi.key.mipsemulator.storage.MemoryType;
import indi.key.mipsemulator.util.IoUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SegmentController implements TickCallback {

    private Machine machine;
    private GraphicsContext gc;
    private double length, padding;
    private SegmentMemory segmentMem;

    private static final double LINE_WIDTH = 4.5;
    private static final Color RED = Color.RED;
    private static final Color GRAY = Color.rgb(220, 220, 220);
    private static final int[] SEG_MAP_2 = new int[]{
            0, 4, 16, 25, 17, 5, 12, 24,
            1, 6, 18, 27, 19, 7, 13, 26,
            2, 8, 20, 29, 21, 9, 14, 28,
            3, 10, 22, 31, 23, 11, 15, 30
    };
    private static final int[] SEG_MAP = new int[]{
            0, 5, 17, 25, 16, 4, 12, 24,
            1, 7, 19, 27, 18, 6, 13, 26,
            2, 9, 21, 29, 20, 8, 14, 28,
            3, 11, 23, 31, 22, 10, 15, 30
    };
    private static final int[] LINE_INDEX = new int[]{
            0, 0, 0,
            1, 0, 1,
            1, 1, 1,
            0, 2, 0,
            0, 1, 1,
            0, 0, 1,
            0, 1, 0
    };
    private static final int[] HEX_SEG_MAP = new int[]{
            0b1000000, 0b1111001, 0b0100100, 0b0110000,
            0b0011001, 0b0010010, 0b0000010, 0b1111000,
            0b0000000, 0b0010000, 0b0001000, 0b0000011,
            0b1000110, 0b0100001, 0b0000110, 0b0001110
    };

    public SegmentController(Canvas canvas, Machine machine) {
        this.machine = machine;
        this.gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(LINE_WIDTH);
        length = canvas.getWidth() / 16;
        padding = canvas.getHeight() / 2 - length;
        segmentMem = (SegmentMemory) machine.getAddressRedirector().getMemory(MemoryType.SEGMENT);
        TimingRenderer.register(this);
    }


    /**
     * show 32 * 2 bits as graph.
     *
     * @param low  the right side of graph, 32bits
     * @param high the left side of graph, 32bits
     */
    private void showGraph(int low, int high) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        BitArray lowBitArray = BitArray.ofValue(low);
        BitArray highBitArray = BitArray.ofValue(high);
        byte[] lowBytes = mapToGraph(lowBitArray); // 4*8 bits
        byte[] highBytes = mapToGraph(highBitArray); // 4*8 bits
        for (int i = 0; i < 4; i++) {
            showNumber(lowBytes[i], 7 - i);
        }
        for (int i = 4; i < 8; i++) {
            showNumber(highBytes[i - 4], 7 - i);
        }
    }

    private byte[] mapToGraph(BitArray raw) {
        BitArray result = BitArray.ofLength(32);
        for (int i = 0; i < 32; i++) {
            result.set(i, raw.get(SEG_MAP[i % 32]));
        }
        return result.bytes();
    }

    /**
     * show 32 bits as text.
     *
     * @param hexes 32 bits data
     * @param point 8 bits points
     */
    private void showText(int hexes, byte point) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        BitArray bitArray = BitArray.ofValue(hexes);
        BitArray pointBits = BitArray.of(point, 8);
        for (int i = 0; i < 8; i++) {
            int hex = bitArray.subArray(i * 4, i * 4 + 4).value();
            BitArray hexBitArray = BitArray.of(HEX_SEG_MAP[hex], 8);
            hexBitArray.set(7, pointBits.get(i));
            showNumber(hexBitArray.bytes()[0], 7 - i);
        }
    }

    /**
     * showNumber a given number with its point to the segment canvas.
     *
     * @param data  the 8-bits data represents a number.
     * @param index the index from left
     */
    private void showNumber(byte data, int index) {
        int k = 1;
        for (int i = 0; i < 21; i += 3) {
            drawLine(data & k, index, LINE_INDEX[i], LINE_INDEX[i + 1], LINE_INDEX[i + 2]);
            k <<= 1;
        }
        drawPoint(data & k, index);
    }

    @SuppressWarnings("SuspiciousNameCombination") // stupid warnings from IDEA, orz...
    private void drawLine(int flag, int index, int xIndex1, int yIndex1, int orientation) {
        double x1 = (index * 2 + xIndex1) * length;
        double y1 = padding + yIndex1 * length;
        double x2, y2;
        if (orientation == 1) { // Vertical
            x2 = x1;
            y2 = y1 + length;
            y1 += LINE_WIDTH;
            y2 -= LINE_WIDTH;
        } else {
            y2 = y1;
            x2 = x1 + length;
            x1 += LINE_WIDTH;
            x2 -= LINE_WIDTH;
        }
        gc.setStroke((flag == 0) ? RED : GRAY);
        gc.strokeLine(x1 + LINE_WIDTH, y1, x2 + LINE_WIDTH, y2);
    }

    private void drawPoint(int flag, int index) {
        gc.setFill((flag == 0) ? RED : GRAY);
        double x = (index * 2 + 1) * length + 2 * LINE_WIDTH;
        double y = padding + length * 2 + LINE_WIDTH / 2;
        gc.fillOval(x, y - LINE_WIDTH, LINE_WIDTH, LINE_WIDTH);
    }

    @Override
    public void onTick(long ticks) {
        boolean text = machine.getSwitches().get(0);
        if (text) {
            showText(segmentMem.getText(), (byte) ~0);
        } else {
            showGraph(segmentMem.getGraphLow(), segmentMem.getGraphHigh());
        }
    }

    public static class SegmentMemory implements Memory {

        private ByteArrayMemory highGraph, lowGraph, text;

        public SegmentMemory(int depth) {
            highGraph = new ByteArrayMemory(32);
            lowGraph = new ByteArrayMemory(32);
            text = new ByteArrayMemory(32);
        }

        @Override
        public void save(long address, byte[] bytes) throws MemoryOutOfBoundsException {
            text.save(0, bytes);
            if ((address & 1) == 0) {
                lowGraph.save(0, bytes);
            } else {
                highGraph.save(0, bytes);
            }
        }

        @Override
        public byte[] load(long address, int bytesNum) throws MemoryOutOfBoundsException {
            return text.load(0, bytesNum);
        }

        @Override
        public byte[] loadConstantly(long address, int bytesNum) throws MemoryOutOfBoundsException {
            return load(address, bytesNum);
        }

        int getText() {
            return IoUtils.bytesToInt(text.load(0, 4));
        }

        int getGraphLow() {
            return IoUtils.bytesToInt(lowGraph.load(0, 4));
        }

        int getGraphHigh() {
            return IoUtils.bytesToInt(highGraph.load(0, 4));
        }

        @Override
        public void reset() {
            highGraph.reset();
            lowGraph.reset();
            text.reset();
        }
    }
}
