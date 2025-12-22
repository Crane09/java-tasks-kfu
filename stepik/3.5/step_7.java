import java.util.function.DoubleUnaryOperator;

public class step_7 {

    public static double integrate(DoubleUnaryOperator f, double a, double b) {
        final double step = 1e-6;
        double result = 0.0;
        double x = a;

        while (x < b) {
            double next = Math.min(x + step, b);
            result += f.applyAsDouble(x) * (next - x);
            x = next;
        }

        return result;
    }
}
