

public class step_12 {
    public static boolean doubleExpression(double a, double b, double c) {
        if (a+b==c || (a+b-c<0.0001 && a+b-c>-0.0001)){
            return true;
        }
        else{
            return false;
        }
}      
}
