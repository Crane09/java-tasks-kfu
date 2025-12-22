public class step_6 {
    public static boolean booleanExpression(boolean a, boolean b, boolean c, boolean d) {
    return (a && b && !c && !d) ||
           (a && !b && c && !d) ||
           (a && !b && !c && d) ||
           (!a && b && c && !d) ||
           (!a && b && !c && d) ||
           (!a && !b && c && d);
}
}
