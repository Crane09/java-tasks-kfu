public class step_8 {
    public static <T, U> java.util.function.Function<T, U> ternaryOperator(
        java.util.function.Predicate<? super T> condition,
        java.util.function.Function<? super T, ? extends U> ifTrue,
        java.util.function.Function<? super T, ? extends U> ifFalse) {

    return t -> condition.test(t) ? ifTrue.apply(t) : ifFalse.apply(t);
}
}
