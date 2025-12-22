public class step_10 {
    public static String getCallerClassAndMethodName() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

    /*
     * stackTrace:
     * [0] getStackTrace
     * [1] getCallerClassAndMethodName
     * [2] метод, который вызвал getCallerClassAndMethodName
     * [3] метод, который вызвал [2], и т.д.
     */

    if (stackTrace.length < 4) {
        return null;
    }

    StackTraceElement caller = stackTrace[3];
    return caller.getClassName() + "#" + caller.getMethodName();
}
}
