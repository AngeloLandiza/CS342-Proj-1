package weather.model;

/**
 * Every weather condition the application understands.
 * Each provider maps its own codes onto these (all of them or a subset).
 */
public enum WeatherCondition {
    // Sky
    CLEAR_SKY("clear sky"),
    MAINLY_CLEAR("mainly clear"),
    PARTLY_CLOUDY("partly cloudy"),
    OVERCAST("overcast"),

    // Fog
    FOG("fog"),
    RIME_FOG("depositing rime fog"),

    // Drizzle
    LIGHT_DRIZZLE("light drizzle"),
    MODERATE_DRIZZLE("moderate drizzle"),
    DENSE_DRIZZLE("dense drizzle"),
    LIGHT_FREEZING_DRIZZLE("light freezing drizzle"),
    DENSE_FREEZING_DRIZZLE("dense freezing drizzle"),

    // Rain
    SLIGHT_RAIN("slight rain"),
    MODERATE_RAIN("moderate rain"),
    HEAVY_RAIN("heavy rain"),
    LIGHT_FREEZING_RAIN("light freezing rain"),
    HEAVY_FREEZING_RAIN("heavy freezing rain"),
    SLIGHT_RAIN_SHOWERS("slight rain showers"),
    MODERATE_RAIN_SHOWERS("moderate rain showers"),
    VIOLENT_RAIN_SHOWERS("violent rain showers"),

    // Snow
    SLIGHT_SNOW("slight snowfall"),
    MODERATE_SNOW("moderate snowfall"),
    HEAVY_SNOW("heavy snowfall"),
    SNOW_GRAINS("snow grains"),
    SLIGHT_SNOW_SHOWERS("slight snow showers"),
    HEAVY_SNOW_SHOWERS("heavy snow showers"),

    // Thunderstorm
    THUNDERSTORM("thunderstorm"),
    THUNDERSTORM_SLIGHT_HAIL("thunderstorm with slight hail"),
    THUNDERSTORM_HEAVY_HAIL("thunderstorm with heavy hail");

    private final String description;

    WeatherCondition(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
