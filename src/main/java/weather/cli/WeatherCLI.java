package weather.cli;

import weather.model.Location;
import weather.model.WeatherComparison;
import weather.model.WeatherData;
import weather.provider.WeatherProviderException;
import weather.service.WeatherService;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WeatherCLI {
    private static final String HELP = """
            help                             Show the available commands
            locations                        List the known locations
            current <location>               Show the current weather for a location
            compare <location1> <location2>  Compare the weather of two locations
            summary <location>               Summarize the conditions for a location
            all                              Compare all locations, warmest first
            quit                             Exit the application
            Put names with spaces in quotes, e.g. current "New York\"""";

    private static final Pattern WORD = Pattern.compile("\"([^\"]*)\"|(\\S+)");

    private final WeatherService service;
    private final Scanner in;
    private final PrintStream out;

    public WeatherCLI(WeatherService service) {
        this(service, System.in, System.out);
    }

    public WeatherCLI(WeatherService service, InputStream in, PrintStream out) {
        this.service = service;
        this.in = new Scanner(in);
        this.out = out;
    }

    public void run() {
        out.println("Weather Information Service");

        while (true) {
            out.print("weather> ");
            if (!in.hasNextLine()) break;

            List<String> words = split(in.nextLine());
            if (words.isEmpty()) continue;
            if (words.get(0).equals("quit")) break;

            out.println(execute(words.get(0), words.subList(1, words.size())));
        }

        out.println("Goodbye!");
    }

    private String execute(String command, List<String> args) {
        try {
            switch (command) {
                case "help":
                    return HELP;
                case "locations":
                    return locations();
                case "current":
                    return current(args);
                case "compare":
                    return compare(args);
                case "summary":
                    return summary(args);
                case "all":
                    return all();
                default:
                    return "Unknown command: " + command + ". Type 'help' to see the available commands.";
            }
        } catch (IllegalArgumentException | WeatherProviderException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String locations() {
        String result = "Known locations:";
        for (Location location : service.getLocations()) {
            result += "\n  " + location.name();
        }
        return result;
    }

    private String current(List<String> args) throws WeatherProviderException {
        if (args.size() != 1) return "Usage: current <location>";

        WeatherData data = service.getCurrentWeather(args.get(0));
        return String.format("""
                %s
                  Temperature:   %.1f°C (feels like %.1f°C)
                  Conditions:    %s
                  Humidity:      %.0f%%
                  Wind:          %.1f km/h
                  Precipitation: %.1f mm""",
                data.location().name(),
                data.temperatureCelsius(), data.feelsLikeCelsius(),
                data.condition().description(),
                data.humidityPercent(),
                data.windSpeedKmh(),
                data.precipitationMm());
    }

    private String compare(List<String> args) throws WeatherProviderException {
        if (args.size() != 2) return "Usage: compare <location1> <location2>";

        WeatherComparison comparison = service.compare(args.get(0), args.get(1));
        WeatherData first = comparison.first();
        WeatherData second = comparison.second();
        return String.format("""
                %s vs %s
                  Temperature:   %.1f°C vs %.1f°C (%+.1f°C)
                  Feels like:    %.1f°C vs %.1f°C (%+.1f°C)
                  Humidity:      %.0f%% vs %.0f%% (%+.0f%%)
                  Wind:          %.1f vs %.1f km/h (%+.1f km/h)
                  Precipitation: %.1f vs %.1f mm (%+.1f mm)
                  Conditions:    %s vs %s""",
                first.location().name(), second.location().name(),
                first.temperatureCelsius(), second.temperatureCelsius(), comparison.temperatureDifference(),
                first.feelsLikeCelsius(), second.feelsLikeCelsius(), comparison.feelsLikeDifference(),
                first.humidityPercent(), second.humidityPercent(), comparison.humidityDifference(),
                first.windSpeedKmh(), second.windSpeedKmh(), comparison.windSpeedDifference(),
                first.precipitationMm(), second.precipitationMm(), comparison.precipitationDifference(),
                first.condition().description(), second.condition().description());
    }

    private String summary(List<String> args) throws WeatherProviderException {
        if (args.size() != 1) return "Usage: summary <location>";

        return service.getSummary(args.get(0));
    }

    private String all() throws WeatherProviderException {
        String result = String.format("%-15s %-12s %-10s %-12s %s", "Location", "Temperature", "Humidity", "Wind", "Conditions");
        for (WeatherData data : service.compareAll()) {
            result += String.format("%n%-15s %-12s %-10s %-12s %s",
                    data.location().name(),
                    String.format("%.1f°C", data.temperatureCelsius()),
                    String.format("%.0f%%", data.humidityPercent()),
                    String.format("%.1f km/h", data.windSpeedKmh()),
                    data.condition().description());
        }
        return result;
    }

    private static List<String> split(String line) {
        List<String> words = new ArrayList<>();
        Matcher matcher = WORD.matcher(line);

        while (matcher.find()) {
            if (matcher.group(1) != null) words.add(matcher.group(1));
            else words.add(matcher.group(2));
        }
        return words;
    }
}
