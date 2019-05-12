package indi.key.mipsemulator.util;

import java.util.Arrays;

import javafx.scene.control.TextArea;

public class LogUtils {

    private static TextArea mLogText = null;

    public static void setLogText(TextArea logText) {
        mLogText = logText;
    }

    public static void i(Object... object) {
        String message;
        if (object.length == 1) {
            message = String.valueOf(object[0]);
        } else {
            message = Arrays.toString(object);
        }
        System.out.println(buildMessage(message));
    }

    public static void m(String string) {
        mLogText.setText(mLogText.getText() + string + "\n");
        mLogText.setScrollTop(Double.MAX_VALUE);
    }

    private static String buildMessage(String rawMessage) {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String fullClassName = caller.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return "[" + Thread.currentThread().getName() + "] " + className + "." + caller.getMethodName() + "(): " + rawMessage;
    }

    private LogUtils() {
    }
}
