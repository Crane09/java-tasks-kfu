import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;

public class step_13 {


public class Main {
    public static void main(String[] args) throws Exception {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8)
        );

        // читаем весь текст целиком
        String text = reader.lines().collect(Collectors.joining(" "));

        // разбиваем на слова, считаем частоты
        Map<String, Long> freq = Arrays.stream(text.split("[^\\p{L}\\p{Digit}]+"))
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        // сортируем и выводим топ-10
        freq.entrySet().stream()
                .sorted(
                        Comparator.<Map.Entry<String, Long>>comparingLong(e -> -e.getValue())
                                .thenComparing(Map.Entry::getKey)
                )
                .limit(10)
                .map(Map.Entry::getKey)
                .forEach(System.out::println);
    }
}
}
