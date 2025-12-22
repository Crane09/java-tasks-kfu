package integral_montekarlo;

import java.util.Random;
import java.util.function.DoubleUnaryOperator;


public final class MonteCarloIntegrator {

    private MonteCarloIntegrator() {
    }

    public static double integrate(DoubleUnaryOperator f, double a, double b, long samples, long seed) {
        if (samples <= 0) {
            throw new IllegalArgumentException("samples must be > 0");
        }
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b)) {
            throw new IllegalArgumentException("a and b must be finite numbers");
        }

        double sign = 1.0;
        if (a > b) {
            double tmp = a;
            a = b;
            b = tmp;
            sign = -1.0;
        }

        Random rnd = new Random(seed);
        double sum = 0.0;

        for (long i = 0; i < samples; i++) {
            double x = a + (b - a) * rnd.nextDouble();
            sum += f.applyAsDouble(x);
        }

        double mean = sum / samples;
        return sign * (b - a) * mean;
    }
}
