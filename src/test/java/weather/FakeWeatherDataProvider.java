package weather;

import weather.model.Location;
import weather.model.WeatherCondition;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;

class FakeWeatherDataProvider implements WeatherDataProvider {
    @Override
    public WeatherData getCurrentWeather(Location location) {
        double temperature = 15.0;

        if (location.name().equals("Chicago")) temperature = 10.0;

        return new WeatherData(location, temperature, 8.0, 50.0, 20.0, 0.0, WeatherCondition.CLEAR_SKY);
    }
}
