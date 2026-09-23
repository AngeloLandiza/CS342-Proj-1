package weather.provider;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import weather.model.Location;
import weather.model.WeatherData;

public class OpenMeteoWeatherProvider implements WeatherDataProvider {
    private final String API_URL = "https://api.open-meteo.com/v1";

    private final HttpClient httpClient;
    

    public OpenMeteoWeatherProvider() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public WeatherData getWeatherData(Location location) throws WeatherProviderException {
        // TODO
        return null;
    }


}
