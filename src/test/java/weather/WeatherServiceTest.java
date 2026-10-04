package weather;

import org.junit.jupiter.api.Test;
import weather.model.Location;
import weather.model.WeatherComparison;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;
import weather.provider.WeatherProviderException;
import weather.service.WeatherService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeatherServiceTest {

    private static final Location CHICAGO = new Location("Chicago", 41.85, -87.65);
    private static final Location NEW_YORK = new Location("New York", 40.71, -74.01);

    private final WeatherService service =
            new WeatherService(new FakeWeatherDataProvider(), List.of(CHICAGO, NEW_YORK));

    @Test
    void getLocationsReturnsConfiguredLocations() {
        assertEquals(List.of(CHICAGO, NEW_YORK), service.getLocations());
    }

    @Test
    void findLocationReturnsMatchingLocation() {
        assertEquals(NEW_YORK, service.findLocation("New York"));
    }

    @Test
    void getCurrentWeatherReturnsProviderData() throws Exception {
        WeatherData data = service.getCurrentWeather("Chicago");

        assertEquals(CHICAGO, data.location());
        assertEquals(10.0, data.temperatureCelsius());
    }

    @Test
    void getSummaryDescribesConditions() throws Exception {
        String summary = service.getSummary("Chicago");

        assertTrue(summary.contains("Chicago"));
        assertTrue(summary.contains("cool"));
        assertTrue(summary.contains("clear sky"));
    }

    @Test
    void compareReturnsDifferenceBetweenLocations() throws Exception {
        WeatherComparison comparison = service.compare("Chicago", "New York");

        assertEquals(-5.0, comparison.temperatureDifference());
    }

    @Test
    void compareAllReturnsEveryLocationWarmestFirst() throws Exception {
        List<WeatherData> all = service.compareAll();

        assertEquals(2, all.size());
        assertEquals(NEW_YORK, all.get(0).location());
        assertEquals(CHICAGO, all.get(1).location());
    }

    @Test
    void unknownLocationThrows() {
        IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, () -> service.findLocation("Paris"));

        assertEquals("Unknown location: Paris", error.getMessage());
    }

    @Test
    void providerFailureIsPassedToCaller() throws Exception {
        WeatherDataProvider failingProvider = mock(WeatherDataProvider.class);
        when(failingProvider.getCurrentWeather(any())).thenThrow(new WeatherProviderException("Service down"));
        WeatherService failingService = new WeatherService(failingProvider, List.of(CHICAGO));

        WeatherProviderException error =
                assertThrows(WeatherProviderException.class, () -> failingService.getCurrentWeather("Chicago"));

        assertEquals("Service down", error.getMessage());
    }
}
