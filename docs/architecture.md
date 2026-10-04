# Weather Information Service — Architecture

## File Structure

```
src/
├── main/
│   └── java/
│       └── weather/
│           ├── Main.java
│           ├── model/
│           │   ├── Location.java
│           │   ├── WeatherData.java
│           │   ├── WeatherCondition.java
│           │   └── WeatherComparison.java
│           ├── provider/
│           │   ├── WeatherDataProvider.java
│           │   ├── WeatherProviderException.java
│           │   └── OpenMeteoWeatherProvider.java
│           ├── service/
│           │   └── WeatherService.java
│           └── cli/
│               └── WeatherCLI.java
└── test/
    └── java/
        └── weather/
            ├── FakeWeatherDataProvider.java
            ├── WeatherServiceTest.java
            └── WeatherCLITest.java
```

## Files

**Startup**
- **Main.java**: Builds the provider, `WeatherService` and `WeatherCLI`, then starts the CLI. This is the only file that names a concrete provider.

**model**: plain data shared by every layer
- **Location.java**: A city's name and coordinates.
- **WeatherData.java**: Current readings for a location, always in metric units.
- **WeatherCondition.java**: Every condition the app understands (clear sky, heavy rain, …). Each provider maps its own codes onto these.
- **WeatherComparison.java**: Two `WeatherData` readings and the differences between them.

**provider**: where weather data comes from
- **WeatherDataProvider.java**: Interface: `getCurrentWeather(Location) → WeatherData`.
- **WeatherProviderException.java**: The one error type every provider throws (network, HTTP status, missing data).
- **OpenMeteoWeatherProvider.java**: Calls Open-Meteo and converts the JSON into `WeatherData`.

**service**: weather logic, with no input/output
- **WeatherService.java**: Looks up locations, gets weather, compares, summarizes and compares all locations, all through `WeatherDataProvider`.

**cli**: user interaction
- **WeatherCLI.java**: Reads commands, checks arguments, calls `WeatherService` and prints the results.

**test**
- **FakeWeatherDataProvider.java**: Returns fixed weather, so tests never use the internet.
- **WeatherServiceTest.java**: Tests the service with the fake provider, plus a Mockito mock for provider failures.
- **WeatherCLITest.java**: Runs commands through the CLI and checks the printed output.

## Adding a Weather Provider

1. Create `provider/XyzWeatherProvider.java` that implements `WeatherDataProvider`.
2. Convert the response into `WeatherData` (metric units, `WeatherCondition`). Throw `WeatherProviderException` on any failure.
3. In `Main`, switch to the new provider.

Nothing in `service`, `cli`, `model` or the tests changes.

## Adding a Command

1. Add a `case` to the `switch` in `WeatherCLI.execute`.
2. Add a method for the command that calls `WeatherService`.
3. Add a line to the `HELP` text.
