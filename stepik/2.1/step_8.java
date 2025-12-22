public class step_8 {
    public static int leapYearCount(int year) {
        int count=0;
        for (int i=1; i<=year; i++){
            if ((i % 4 == 0 && i % 100 != 0) || (i % 400 == 0)) {
                count++;
            }
        }
        return count;
    }

}