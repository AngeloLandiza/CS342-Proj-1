package weather.service;

import weather.model.Location;
import weather.model.WeatherComparison;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;
import weather.provider.WeatherProviderException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class WeatherService {
    private final WeatherDataProvider provider;
    private final List<Location> locations;
    

    private static String describeTemperature(double celsius) {
        if (celsius < 0) return "freezing";
        if (celsius < 10) return "cold";
        if (celsius < 18) return "cool";
        if (celsius < 25) return "mild";
        if (celsius < 30) return "warm";
        return "hot";
    }

    private static String describeWind(double kmh) {
        if (kmh < 10) return "calm";
        if (kmh < 30) return "breezy";
        return "strong";
    }

    public WeatherService(WeatherDataProvider provider, List<Location> locations) {
        this.provider = provider;
        this.locations = List.copyOf(locations);
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Location findLocation(String name) {
        for (Location location : locations) {
            if (location.name().equals(name.strip())) return location;
        }

        throw new IllegalArgumentException("Unknown location: " + name);
    }

    public WeatherData getCurrentWeather(String name) throws WeatherProviderException {
        return provider.getCurrentWeather(findLocation(name));
    }
    
    public String getSummary(String name) throws WeatherProviderException {
        WeatherData data = getCurrentWeather(name);
        String summary = String.format(
                "It is %s in %s at %.1f°C (feels like %.1f°C) with %s. Humidity is %.0f%% and the wind is %s at %.1f km/h.",
                describeTemperature(data.temperatureCelsius()),
                data.location().name(),
                data.temperatureCelsius(),
                data.feelsLikeCelsius(),
                data.condition().description(),
                data.humidityPercent(),
                describeWind(data.windSpeedKmh()),
                data.windSpeedKmh());

        if (data.precipitationMm() > 0) summary += String.format(" %.1f mm of precipitation has fallen.", data.precipitationMm());
        
        return summary;
    }

    public WeatherComparison compare(String first, String second) throws WeatherProviderException {
        return new WeatherComparison(getCurrentWeather(first), getCurrentWeather(second));
    }

    public List<WeatherData> compareAll() throws WeatherProviderException {
        List<WeatherData> all = new ArrayList<>();
        for (Location location : locations) {
            all.add(provider.getCurrentWeather(location));
        }

        all.sort(Comparator.comparingDouble(WeatherData::temperatureCelsius).reversed());
        return all;
    }
}
