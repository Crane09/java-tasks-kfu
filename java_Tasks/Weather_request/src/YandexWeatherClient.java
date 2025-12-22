package weather_request;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class YandexWeatherClient {

    private final HttpClient http;
    private final String apiKey;

    public YandexWeatherClient(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("apiKey is empty");
        }
        this.apiKey = apiKey;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public WeatherInfo getCurrentWeather(double lat, double lon) throws IOException, InterruptedException {
        String url = "https://api.weather.yandex.ru/v2/forecast" +
                "?lat=" + encode(String.valueOf(lat)) +
                "&lon=" + encode(String.valueOf(lon)) +
                "&lang=ru_RU" +
                "&limit=1" +
                "&hours=false";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("X-Yandex-Weather-Key", apiKey)
                .GET()
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        if (code != 200) {
            throw new IOException("HTTP " + code + ": " + response.body());
        }

        return parse(response.body());
    }

    private static String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static WeatherInfo parse(String json) {
        WeatherInfo info = new WeatherInfo();

        info.nowDt = extractString(json, "\"now_dt\"");

        String fact = extractObject(json, "\"fact\"");
        info.temp = extractInt(fact, "\"temp\"");
        info.feelsLike = extractInt(fact, "\"feels_like\"");
        info.condition = extractString(fact, "\"condition\"");
        info.windSpeed = extractDouble(fact, "\"wind_speed\"");
        info.pressureMm = extractInt(fact, "\"pressure_mm\"");
        info.humidity = extractInt(fact, "\"humidity\"");

        return info;
    }

    private static String extractObject(String json, String key) {
        int keyPos = json.indexOf(key);
        if (keyPos < 0) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        int colon = json.indexOf(':', keyPos);
        int start = json.indexOf('{', colon);
        if (start < 0) {
            throw new IllegalArgumentException("Object start not found for key: " + key);
        }
        int depth = 0;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return json.substring(start, i + 1);
                }
            }
        }
        throw new IllegalArgumentException("Object not closed for key: " + key);
    }

    private static String extractString(String json, String key) {
        int keyPos = json.indexOf(key);
        if (keyPos < 0) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        int colon = json.indexOf(':', keyPos);
        int firstQuote = json.indexOf('"', colon + 1);
        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (firstQuote < 0 || secondQuote < 0) {
            throw new IllegalArgumentException("String value not found for key: " + key);
        }
        return json.substring(firstQuote + 1, secondQuote);
    }

    private static int extractInt(String json, String key) {
        String num = extractNumber(json, key);
        return Integer.parseInt(num);
    }

    private static double extractDouble(String json, String key) {
        String num = extractNumber(json, key);
        return Double.parseDouble(num);
    }

    private static String extractNumber(String json, String key) {
        int keyPos = json.indexOf(key);
        if (keyPos < 0) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        int colon = json.indexOf(':', keyPos);
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;

        int start = i;
        while (i < json.length()) {
            char c = json.charAt(i);
            if ((c >= '0' && c <= '9') || c == '-' || c == '+' || c == '.') {
                i++;
            } else {
                break;
            }
        }

        if (start == i) {
            throw new IllegalArgumentException("Number value not found for key: " + key);
        }
        return json.substring(start, i);
    }
}
