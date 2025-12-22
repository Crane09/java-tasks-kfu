package weather_request;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java weather_request.Main <lat> <lon>");
            System.out.println("Also set environment variable YANDEX_WEATHER_KEY.");
            return;
        }

        String key = System.getenv("YANDEX_WEATHER_KEY");
        if (key == null || key.isBlank()) {
            System.out.println("ERROR: env var YANDEX_WEATHER_KEY is not set.");
            return;
        }

        double lat = Double.parseDouble(args[0]);
        double lon = Double.parseDouble(args[1]);

        YandexWeatherClient client = new YandexWeatherClient(key);
        WeatherInfo info = client.getCurrentWeather(lat, lon);

        System.out.println(info);
    }
}
