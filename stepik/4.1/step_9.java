public class step_9 {
    public static double sqrt(double x) {
    if (x < 0) {
        throw new IllegalArgumentException(
            "Expected non-negative number, got " + x
        );
    }
    return Math.sqrt(x);
}
}
