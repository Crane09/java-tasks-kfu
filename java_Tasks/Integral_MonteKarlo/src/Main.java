package integral_montekarlo;

import java.util.Locale;
import java.util.function.DoubleUnaryOperator;

public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); 

        if (args.length < 4) {
            System.out.println("Usage: java integral_montekarlo.Main <function> <a> <b> <samples> [seed]...");
            System.out.println("functions: square, sin, gauss");
            return;
        }

        DoubleUnaryOperator f = parseFunction(args[0]);
        double a = Double.parseDouble(args[1]);
        double b = Double.parseDouble(args[2]);
        long samples = Long.parseLong(args[3]);
        long seed = (args.length >= 5) ? Long.parseLong(args[4]) : System.nanoTime();

        double estimate = MonteCarloIntegrator.integrate(f, a, b, samples, seed);

        System.out.printf("f=%s, a=%.6f, b=%.6f, samples=%d, seed=%d\n", args[0], a, b, samples, seed);
        System.out.printf("Monte-Carlo estimate = %.10f\n", estimate);

        if (args[0].equalsIgnoreCase("square")) {
            double exact = (Math.pow(b, 3) - Math.pow(a, 3)) / 3.0;
            System.out.printf("Exact integral       = %.10f\n", exact);
            System.out.printf("Absolute error       = %.10f\n", Math.abs(estimate - exact));
        }
    }

    private static DoubleUnaryOperator parseFunction(String name) {
        switch (name.toLowerCase()) {
            case "square":
                return Functions.SQUARE;
            case "sin":
                return Functions.SIN;
            case "gauss":
                return Functions.GAUSS;
            default:
                throw new IllegalArgumentException("Unknown function: " + name);
        }
    }
}
