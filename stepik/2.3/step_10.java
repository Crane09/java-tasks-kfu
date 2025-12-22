public class step_10 {
    /**
 * Checks if given <code>text</code> is a palindrome.
 *
 * @param text any string
 * @return <code>true</code> when <code>text</code> is a palindrome, <code>false</code> otherwise
 */
public static boolean isPalindrome(String text) {
    String cleaned = text.replaceAll("[\\W_]", "").toLowerCase();
    int len = cleaned.length();
    for (int i = 0; i < len / 2; i++) {
        if (cleaned.charAt(i) != cleaned.charAt(len - 1 - i)) {
            return false;
        }
    }
    return true;
}
}
