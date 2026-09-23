package weather.provider;

import weather.model.Location;
import weather.model.WeatherData;


public interface WeatherDataProvider {
    public WeatherData getWeatherData(Location location) throws WeatherProviderException;
}