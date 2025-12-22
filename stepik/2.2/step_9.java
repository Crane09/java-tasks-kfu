public class step_9 {
    /**
 * Checks if given <code>value</code> is a power of two.
 *
 * @param value any number
 * @return <code>true</code> when <code>value</code> is power of two, <code>false</code> otherwise
 */
public static boolean isPowerOfTwo(int value) {
    long v = Math.abs((long) value);
    return v != 0 && ((v & (v - 1)) == 0);
}
}
