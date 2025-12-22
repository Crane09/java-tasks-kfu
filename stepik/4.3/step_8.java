public class step_8 {
    private static void configureLogging() {

    // ClassA — все уровни
    java.util.logging.Logger.getLogger(
            "org.stepic.java.logging.ClassA"
    ).setLevel(java.util.logging.Level.ALL);

    // ClassB — WARNING и выше
    java.util.logging.Logger.getLogger(
            "org.stepic.java.logging.ClassB"
    ).setLevel(java.util.logging.Level.WARNING);

    // Общий handler (консоль + XML)
    java.util.logging.ConsoleHandler handler =
            new java.util.logging.ConsoleHandler();
    handler.setLevel(java.util.logging.Level.ALL);
    handler.setFormatter(new java.util.logging.XMLFormatter());

    // org.stepic.java.logging
    java.util.logging.Logger loggingLogger =
            java.util.logging.Logger.getLogger("org.stepic.java.logging");
    loggingLogger.setLevel(java.util.logging.Level.ALL);
    loggingLogger.setUseParentHandlers(false);
    loggingLogger.addHandler(handler);

    // org.stepic.java  ← ВАЖНО!
    java.util.logging.Logger javaLogger =
            java.util.logging.Logger.getLogger("org.stepic.java");
    javaLogger.setLevel(java.util.logging.Level.ALL);
    javaLogger.setUseParentHandlers(false);
    javaLogger.addHandler(handler);
}
}
