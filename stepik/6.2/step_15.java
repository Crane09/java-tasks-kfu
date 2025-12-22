package Java.stepik.6.2;

public class step_15 {
    import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> list = new ArrayList<>();

        while (scanner.hasNextInt()) {
            list.add(scanner.nextInt());
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            if (i % 2 != 0) {
                System.out.print(list.get(i));
                if (i > 1) {
                    System.out.print(" ");
                }
            }
        }
    }
}
}
