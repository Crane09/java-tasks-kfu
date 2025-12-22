public class step_12 {
    public static <T> void findMinMax(
        java.util.stream.Stream<? extends T> stream,
        java.util.Comparator<? super T> order,
        java.util.function.BiConsumer<? super T, ? super T> minMaxConsumer) {

    java.util.Iterator<? extends T> it = stream.iterator();

    if (!it.hasNext()) {
        minMaxConsumer.accept(null, null);
        return;
    }

    T min = it.next();
    T max = min;

    while (it.hasNext()) {
        T current = it.next();
        if (order.compare(current, min) < 0) {
            min = current;
        }
        if (order.compare(current, max) > 0) {
            max = current;
        }
    }

    minMaxConsumer.accept(min, max);
}
}
