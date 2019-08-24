package indi.key.mipsemulator.model.exception;

public class EmulatorException extends RuntimeException {
    public EmulatorException() {
        super();
    }

    public EmulatorException(String message) {
        super(message);
    }

    public EmulatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmulatorException(Throwable cause) {
        super(cause);
    }

    protected EmulatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
