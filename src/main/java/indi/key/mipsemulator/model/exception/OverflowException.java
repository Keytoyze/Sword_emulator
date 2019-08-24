package indi.key.mipsemulator.model.exception;

public class OverflowException extends EmulatorException {

    public OverflowException() {
        super();
    }

    public OverflowException(String message) {
        super(message);
    }

    public OverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverflowException(Throwable cause) {
        super(cause);
    }

    protected OverflowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
