package indi.key.mipsemulator.model.exception;

public class MemoryOutOfBoundsException extends EmulatorException {
    public MemoryOutOfBoundsException() {
        super();
    }

    public MemoryOutOfBoundsException(String message) {
        super(message);
    }

    public MemoryOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemoryOutOfBoundsException(Throwable cause) {
        super(cause);
    }

    protected MemoryOutOfBoundsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
