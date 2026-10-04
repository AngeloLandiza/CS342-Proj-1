package weather.model;

public record WeatherComparison(WeatherData first, WeatherData second) {

    public double temperatureDifference() {
        return first.temperatureCelsius() - second.temperatureCelsius();
    }

    public double feelsLikeDifference() {
        return first.feelsLikeCelsius() - second.feelsLikeCelsius();
    }

    public double humidityDifference() {
        return first.humidityPercent() - second.humidityPercent();
    }

    public double windSpeedDifference() {
        return first.windSpeedKmh() - second.windSpeedKmh();
    }

    public double precipitationDifference() {
        return first.precipitationMm() - second.precipitationMm();
    }
}
