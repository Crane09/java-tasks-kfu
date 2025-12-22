package integral_montekarlo;

import java.util.function.DoubleUnaryOperator;

public final class Functions {

    private Functions() {
    }

    /** f(x) = x^2 */
    public static final DoubleUnaryOperator SQUARE = x -> x * x;

    /** f(x) = sin(x) */
    public static final DoubleUnaryOperator SIN = Math::sin;

    /** f(x) = exp(-x^2) */
    public static final DoubleUnaryOperator GAUSS = x -> Math.exp(-x * x);
}
