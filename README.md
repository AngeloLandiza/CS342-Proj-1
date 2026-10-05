# CS 342 Weather Information Service

## Team Members

- Angelo Landiza

## Running the Application

You need Java 25 and Maven.

```bash
mvn compile
java -cp target/classes weather.Main
```

Commands:

| Command                             | What it does                          |
| ----------------------------------- | ------------------------------------- |
| `help`                            | Shows all commands                    |
| `locations`                       | Lists the cities the app knows        |
| `current <location>`              | Shows the weather for one city        |
| `compare <location1> <location2>` | Compares the weather of two cities    |
| `summary <location>`              | Describes the weather in one sentence |
| `all`                             | Shows every city, warmest first       |
| `quit`                            | Exits the app                         |

Use quotes for names with spaces EX. `current "New York"`.

## Running the Tests

```bash
mvn test
```

## Design

| File                                  | Job                                                                    |
| ------------------------------------- | ---------------------------------------------------------------------- |
| `Main`                              | Creates all the objects and starts the app                             |
| `model/Location`                    | Data model for a city's name and coordinates                          |
| `model/WeatherData`                 | Data model for the weather readings for a city                         |
| `model/WeatherCondition`            | Data model for the list of weather types (clear sky, rain, snow, etc.) |
| `model/WeatherComparison`           | Two cities' weather and the differences                                |
| `provider/WeatherDataProvider`      | Interface for getting weather data                                     |
| `provider/WeatherProviderException` | Weather-related error                                                  |
| `provider/OpenMeteoWeatherProvider` | Gets the weather from Open-Meteo API                                   |
| `service/WeatherService`            | The weather logic: find a city, compare, summarize                     |
| `cli/WeatherCLI`                    | Reads commands and prints results to terminal                          |

If something goes wrong (a bad command, an unknown city, no internet, or bad data), the app prints an error, doesn't use mock data, and keeps running

## Interfaces

`WeatherDataProvider` is the interface for getting weather. `WeatherService` only talks to this interface, so it doesn't care whether the data comes from Open-Meteo API, a different weather provider, or a fake used in tests.

## Dependency Injection

`WeatherService` is given its `WeatherDataProvider` through its constructor instead of creating one itself. `Main` passes in its own Open-Meteo provider, and the tests pass in a fake one.

## Testing

The tests use two fake providers instead of the real API:

- `FakeWeatherDataProvider` always returns the same weather.
- `FailingWeatherDataProvider` always throws an error.

`WeatherServiceTest`- (8 tests) checks the service logic. `WeatherCLITest` -(9 tests) types commands into the CLI and checks what it prints.

## Java 25

- Records - Data classes (`Location`, `WeatherData`, `WeatherComparison`)
- Text Blocks - for the help text and multi-line output, so they're easy to read.
- Switch-case - Turns Open-Meteo's weather codes into `WeatherCondition` values.

## Additional Feature

**Compare all locations.** The `all` command shows the weather for every city in one table, sorted from warmest to coldest:

```
weather> all
Location        Temperature  Humidity   Wind         Conditions
Los Angeles     25.1°C       40%        9.8 km/h     clear sky
New York        15.4°C       85%        14.0 km/h    partly cloudy
Chicago         10.7°C       86%        11.4 km/h    clear sky
```

## Design Reflection Questions

**1. What responsibilities were combined in the original starter code that you separated during refactoring?**

The starter's `Main` did everything at once, like making the HTTP request and printing results. Now data retrival happens in `OpenMeteoWeatherProvider` and printing happens in `WeatherCLI`.

**2. How does programming to `WeatherDataProvider` make the program easier to change? **

It makes it easier becuase to use a different weather API, you only write a new class that implements `WeatherDataProvider` and the rest of the program doesn't change.

**3. How does dependency injection make `WeatherService` easier to test?**

Dependency injection allows tests to give `WeatherService` a fake provider with set values so the results are always predictable. For example, the fake makes Chicago 10 °C and New York 15 °C, so the comparison is always −5 °C.

**4. Why should unit tests avoid depending on the live weather API?**

The tests would fail if there is no internet and using live data means there is a lack of control and there is no way to compare against some ground truth in order to verify reliability

**5. If the current weather API were replaced with a completely different provider, which parts of your application would need to change? Which parts should not need to change?**

Only a new provider class and one line in `Main` would change. `WeatherService`, `WeatherCLI`, the model classes and the tests would stay the same.
