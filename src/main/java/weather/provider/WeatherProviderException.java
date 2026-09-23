package weather.provider;

public class WeatherProviderException extends Exception {
    public WeatherProviderException() {
        super();
    }

    public WeatherProviderException(String message) {
        super(message);
    }

    public WeatherProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
