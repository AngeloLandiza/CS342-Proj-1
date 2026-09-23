package weather;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.IO.println;

// STARTER CODE:
// This class intentionally contains several responsibilities.
// Refactor it into the required model, provider, service, and CLI packages.
record TargetLocation(String city, String lat, String lon) {}

public class Main {

    public static void main(String[] args) {
        println("🌤️ Initializing Real-Time Multi-City Weather Service...");

        List<TargetLocation> locations = List.of(
                new TargetLocation("Chicago", "41.85", "-87.65"),
                new TargetLocation("Los Angeles", "34.05", "-118.24"),
                new TargetLocation("New York", "40.71", "-74.01")
        );

        try (HttpClient client = HttpClient.newHttpClient()) {
            for (TargetLocation target : locations) {
                String url = "https://api.open-meteo.com/v1/forecast?latitude=" + target.lat()
                        + "&longitude=" + target.lon()
                        + "&current=temperature_2m&temperature_unit=fahrenheit";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    double temp = parseTemperature(response.body());
                    renderBar(target.city(), temp);
                } else {
                    println("⚠️ " + target.city()
                            + " API Error: Code " + response.statusCode());
                }
            }
        } catch (Exception e) {
            println("❌ Operational Error: " + e.getMessage());
        }
    }

    static double parseTemperature(String json) {
        Pattern pattern = Pattern.compile("\"temperature_2m\":\\s*([0-9.-]+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        // TODO: The final project must replace this silent fallback
        // with an appropriate error-handling strategy.
        return 72.0;
    }

    static void renderBar(String city, double temp) {
        System.out.printf("%-15s | %5.1f°F [", city, temp);
        int barLength = (int) Math.max(0, temp / 2);

        for (int j = 0; j < barLength; j++) {
            System.out.print("■");
        }
        for (int j = barLength; j < 40; j++) {
            System.out.print(" ");
        }
        println("]");
    }
}
