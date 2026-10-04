package weather.provider;

import weather.model.Location;
import weather.model.WeatherData;

/**
 * A source of weather data. Implement this to add a new weather API.
 */
public interface WeatherDataProvider {
    WeatherData getCurrentWeather(Location location) throws WeatherProviderException;
}
