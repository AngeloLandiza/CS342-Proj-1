package weather.provider;

import weather.model.Location;
import weather.model.WeatherCondition;
import weather.model.WeatherData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OpenMeteoWeatherProvider implements WeatherDataProvider {
    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

    private final HttpClient client;

    
    public OpenMeteoWeatherProvider() {
        this(HttpClient.newHttpClient());
    }

    public OpenMeteoWeatherProvider(HttpClient client) {
        this.client = client;
    }

    private String buildUrl(Location location) {
        return BASE_URL
                + "?latitude=" + location.latitude()
                + "&longitude=" + location.longitude()
                + "&current="
                + "temperature_2m,apparent_temperature,relative_humidity_2m,wind_speed_10m,precipitation,weather_code";
    }

    private String fetch(String url) throws WeatherProviderException {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofSeconds(10)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) throw new WeatherProviderException("Open-Meteo returned HTTP " + response.statusCode());

            return response.body();
        } catch (IOException e) {
            throw new WeatherProviderException("Could not reach Open-Meteo", e);
        } catch (InterruptedException e) {
            throw new WeatherProviderException("Request to Open-Meteo was interrupted", e);
        }
    }

    private WeatherCondition toCondition(int code) throws WeatherProviderException {
        return switch (code) {
            case 0 -> WeatherCondition.CLEAR_SKY;
            case 1 -> WeatherCondition.MAINLY_CLEAR;
            case 2 -> WeatherCondition.PARTLY_CLOUDY;
            case 3 -> WeatherCondition.OVERCAST;
            case 45 -> WeatherCondition.FOG;
            case 48 -> WeatherCondition.RIME_FOG;
            case 51 -> WeatherCondition.LIGHT_DRIZZLE;
            case 53 -> WeatherCondition.MODERATE_DRIZZLE;
            case 55 -> WeatherCondition.DENSE_DRIZZLE;
            case 56 -> WeatherCondition.LIGHT_FREEZING_DRIZZLE;
            case 57 -> WeatherCondition.DENSE_FREEZING_DRIZZLE;
            case 61 -> WeatherCondition.SLIGHT_RAIN;
            case 63 -> WeatherCondition.MODERATE_RAIN;
            case 65 -> WeatherCondition.HEAVY_RAIN;
            case 66 -> WeatherCondition.LIGHT_FREEZING_RAIN;
            case 67 -> WeatherCondition.HEAVY_FREEZING_RAIN;
            case 71 -> WeatherCondition.SLIGHT_SNOW;
            case 73 -> WeatherCondition.MODERATE_SNOW;
            case 75 -> WeatherCondition.HEAVY_SNOW;
            case 77 -> WeatherCondition.SNOW_GRAINS;
            case 80 -> WeatherCondition.SLIGHT_RAIN_SHOWERS;
            case 81 -> WeatherCondition.MODERATE_RAIN_SHOWERS;
            case 82 -> WeatherCondition.VIOLENT_RAIN_SHOWERS;
            case 85 -> WeatherCondition.SLIGHT_SNOW_SHOWERS;
            case 86 -> WeatherCondition.HEAVY_SNOW_SHOWERS;
            case 95 -> WeatherCondition.THUNDERSTORM;
            case 96 -> WeatherCondition.THUNDERSTORM_SLIGHT_HAIL;
            case 99 -> WeatherCondition.THUNDERSTORM_HEAVY_HAIL;
            default -> throw new WeatherProviderException("Unknown weather code " + code);
        };
    }

    private double readNumber(String json, String field) throws WeatherProviderException {
        Matcher matcher = Pattern.compile("\"" + field + "\":\\s*(-?[0-9.]+)").matcher(json);

        if (!matcher.find()) throw new WeatherProviderException("Response is missing " + field);

        return Double.parseDouble(matcher.group(1));
    }

    private WeatherData parse(String json, Location location) throws WeatherProviderException {
        return new WeatherData(
                location,
                readNumber(json, "temperature_2m"),
                readNumber(json, "apparent_temperature"),
                readNumber(json, "relative_humidity_2m"),
                readNumber(json, "wind_speed_10m"),
                readNumber(json, "precipitation"),
                toCondition((int) readNumber(json, "weather_code")));
    }

    @Override
    public WeatherData getCurrentWeather(Location location) throws WeatherProviderException {
        String json = fetch(buildUrl(location));
        return parse(json, location);
    }
}
