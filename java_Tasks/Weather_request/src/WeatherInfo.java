package weather_request;

public class WeatherInfo {
    public String nowDt;
    public int temp;
    public int feelsLike;
    public String condition;
    public double windSpeed;
    public int pressureMm;
    public int humidity;

    @Override
    public String toString() {
        return "now_dt=" + nowDt +
                ", temp=" + temp + "°C" +
                ", feels_like=" + feelsLike + "°C" +
                ", condition='" + condition + '\'' +
                ", wind_speed=" + windSpeed + " m/s" +
                ", pressure_mm=" + pressureMm +
                ", humidity=" + humidity + "%";
    }
}
