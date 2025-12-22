public class step_11 {
    public static java.util.stream.IntStream pseudoRandomStream(int seed) {
    return java.util.stream.IntStream.iterate(seed, x -> {
        int sq = x * x;
        return (sq / 10) % 1000;
    });
}

}
