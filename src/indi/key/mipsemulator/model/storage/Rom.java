package indi.key.mipsemulator.model.storage;

import java.io.File;

public class Rom extends BaseMemory {

    public Rom(File file) {
        super(file);
    }

    public Rom(int depth) {
        super(depth);
    }

    @Override
    public String getType() {
        return "ROM";
    }
}
