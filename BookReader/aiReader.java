package JavaBookReader.BookReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.http.HttpClient;

public class aiReader {
    public static void main(String[] args) {
        bomReader();
        String model = "deepseek-r1:14b";
        System.out.println("What is your input? ");
        Scanner scanner = new Scanner(System.in);
        String prompt = scanner.nextLine();
        scanner.close();
        Thread ollamaThread = new Thread(() -> {
            genericOllamaRequest(model, prompt);
        });
        ollamaThread.start();
        try {
            ollamaThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void bomReader() {
        try {
            File bookOfMormon = new File("JavaBookReader\\BookReader\\Book_of_Mormon.txt");
            Scanner reader = new Scanner(bookOfMormon);
            System.out.println("Book of Mormon");
            for (int i = 0; i <= 10; i++) {
                String line = reader.nextLine();
                ArrayList<String> verse = new ArrayList<>();
                if (line != "\n") {
                    verse.add(line);
                }
                System.out.println(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Your code made a stinky");
            e.printStackTrace();
        }
    }

    public static void genericOllamaRequest(String model, String prompt) {
        System.out.println("Model: " + model);
        System.out.println("Prompt: " + prompt);
        String api = "http://localhost:11434/api/generate";
        String jsonBody = String.format("""
                {
                    "model": "%s",
                    "prompt": "%s"
                }
                                    """, model, prompt);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
        ;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            // StringBuilder generatedText = new StringBuilder();
            System.out.println(response);
            System.out.println("Your response was: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
