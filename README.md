# CS 342 Weather Information Service — Starter Project

This Maven project contains the initial weather application to be refactored for the course project.

## Requirements

- Java 25
- Maven
- JUnit Jupiter

## Compile

```bash
mvn compile
```

## Run tests

```bash
mvn test
```

## Required organization

The directories for `model`, `provider`, `service`, and `cli` are already included.
Refactor the starter `Main.java` into the required architecture described in the project assignment.

The starter code intentionally remains tightly coupled. Do not treat its current organization as the desired final design.

## Unit-test minimums

- `WeatherServiceTest.java`: at least 8 meaningful JUnit tests
- `WeatherCLITest.java`: at least 5 meaningful JUnit tests

The final unit tests must not depend on a live Internet connection or make real requests to Open-Meteo.

## Weather API documentation

https://open-meteo.com/en/docs
