package weather.model;

/**
 * Current conditions at a location, always in metric units.
 * Providers must convert into these units before constructing it.
 */
public record WeatherData(
        Location location,
        double temperatureCelsius,
        double feelsLikeCelsius,
        double humidityPercent,
        double windSpeedKmh,
        double precipitationMm,
        WeatherCondition condition) {
}
