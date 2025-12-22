public class step_14 {
    public static <T> Set<T> symmetricDifference(Set<? extends T> set1,
                                             Set<? extends T> set2) {

    Set<T> result = new HashSet<>();

    for (T element : set1) {
        if (!set2.contains(element)) {
            result.add(element);
        }
    }

    for (T element : set2) {
        if (!set1.contains(element)) {
            result.add(element);
        }
    }

    return result;
}
}
