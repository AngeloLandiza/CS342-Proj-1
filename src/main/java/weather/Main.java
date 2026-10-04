package weather;

import weather.cli.WeatherCLI;
import weather.model.Location;
import weather.provider.OpenMeteoWeatherProvider;
import weather.provider.WeatherDataProvider;
import weather.service.WeatherService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Location> locations = List.of(
                new Location("Chicago", 41.85, -87.65),
                new Location("Los Angeles", 34.05, -118.24),
                new Location("New York", 40.71, -74.01));

        WeatherDataProvider provider = new OpenMeteoWeatherProvider();
        WeatherService service = new WeatherService(provider, locations);
        WeatherCLI cli = new WeatherCLI(service);

        cli.run();
    }
}
