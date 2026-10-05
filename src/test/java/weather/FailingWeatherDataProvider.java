package weather;

import weather.model.Location;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;
import weather.provider.WeatherProviderException;


class FailingWeatherDataProvider implements WeatherDataProvider {
    @Override
    public WeatherData getCurrentWeather(Location location) throws WeatherProviderException {
        throw new WeatherProviderException("Service down");
    }
}
