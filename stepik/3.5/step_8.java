import java.nio.charset.StandardCharsets;
public class step_8{
public final class AsciiCharSequence implements CharSequence {
    private final byte[] value;
    private final int start;
    private final int end;

    public AsciiCharSequence(byte[] value) {
        this(value.clone(), 0, value.length);
    }

    private AsciiCharSequence(byte[] value, int start, int end) {
        this.value = value;
        this.start = start;
        this.end = end;
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public char charAt(int index) {
        return (char) (value[start + index] & 0xFF);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new AsciiCharSequence(value, this.start + start, this.start + end);
    }

    @Override
    public String toString() {
        return new String(value, start, length(), StandardCharsets.US_ASCII);
}
}
}