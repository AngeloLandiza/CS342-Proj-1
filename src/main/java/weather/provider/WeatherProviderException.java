package weather.provider;

/**
 * Exception thrown when an error occurs while fetching weather data from a provider.
 */
public class WeatherProviderException extends Exception {
    public WeatherProviderException(String message) {
        super(message);
    }

    public WeatherProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
