import java.math.BigInteger;

public class step_8 {
    /**
 * Calculates factorial of given <code>value</code>.
 *
 * @param value positive number
 * @return factorial of <code>value</code>
 */
public static BigInteger factorial(int value) {
    BigInteger n=BigInteger.ONE;
    for (int i=2;i<=value;i++){
        n=n.multiply(BigInteger.valueOf(i));
    }
    return n; // your implementation here
}
}
