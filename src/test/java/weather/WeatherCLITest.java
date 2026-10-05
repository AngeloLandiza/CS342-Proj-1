package weather;

import org.junit.jupiter.api.Test;
import weather.cli.WeatherCLI;
import weather.model.Location;
import weather.service.WeatherService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class WeatherCLITest {
    private static final List<Location> LOCATIONS = List.of(
            new Location("Chicago", 41.85, -87.65),
            new Location("New York", 40.71, -74.01));

    private String run(String input) {
        WeatherService service = new WeatherService(new FakeWeatherDataProvider(), LOCATIONS);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        WeatherCLI cli = new WeatherCLI(
                service,
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(output, true, StandardCharsets.UTF_8));
        cli.run();

        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void helpListsCommands() {
        String output = run("help\n");

        assertTrue(output.contains("compare <location1> <location2>"));
    }

    @Test
    void locationsListsKnownLocations() {
        String output = run("locations\n");

        assertTrue(output.contains("Chicago"));
        assertTrue(output.contains("New York"));
    }

    @Test
    void currentShowsWeatherForLocation() {
        String output = run("current Chicago\n");

        assertTrue(output.contains("10.0°C"));
    }

    @Test
    void compareShowsDifferenceBetweenLocations() {
        String output = run("compare Chicago \"New York\"\n");

        assertTrue(output.contains("-5.0°C"));
    }

    @Test
    void summaryShowsSentence() {
        String output = run("summary Chicago\n");

        assertTrue(output.contains("It is cool in Chicago"));
    }

    @Test
    void allListsLocationsWarmestFirst() {
        String output = run("all\n");

        assertTrue(output.indexOf("New York") < output.indexOf("Chicago"));
    }

    @Test
    void quitStopsReadingCommands() {
        String output = run("quit\ncurrent Chicago\n");

        assertTrue(output.contains("Goodbye!"));
        assertFalse(output.contains("10.0°C"));
    }

    @Test
    void wrongArgumentCountShowsUsage() {
        String output = run("compare Chicago\n");

        assertTrue(output.contains("Usage: compare <location1> <location2>"));
    }

    @Test
    void unknownCommandShowsError() {
        String output = run("fly Chicago\n");

        assertTrue(output.contains("Unknown command: fly"));
    }
}
