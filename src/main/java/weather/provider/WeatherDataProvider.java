package weather.provider;

import weather.model.Location;
import weather.model.WeatherData;


public interface WeatherDataProvider {
    WeatherData getCurrentWeather(Location location) throws WeatherProviderException;
}
