package indi.key.mipsemulator.util;

public class LogUtils {

    public static void i(Object object) {
        System.out.println(buildMessage(String.valueOf(object)));
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
