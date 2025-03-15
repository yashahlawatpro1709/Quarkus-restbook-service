package org.agoncal.quarkus.starting;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.inject.Inject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@ApplicationScoped
public class OpenAIBookData {

    @ConfigProperty(name = "openai.api.key")
    String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Inject
    LibraryManager libraryManager;

    public String searchBook(String bookTitle) {
        try {
            String requestBody = """
                    {
                        "model": "gpt-3.5-turbo",
                        "messages": [
                            {"role": "system", "content": "You are a book search assistant."},
                            {"role": "user", "content": "Tell me about the book %s"}
                        ],
                        "max_tokens": 200
                    }
                    """.formatted(bookTitle);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String openAIResponse = response.body();

            String extractedTitle = extractField(openAIResponse, "title");
            String extractedAuthor = extractField(openAIResponse, "author");
            String extractedGenre = extractField(openAIResponse, "genre");
            int extractedYear = extractFieldAsInt(openAIResponse, "yearOfPublication", 0);

            if (extractedTitle != null && extractedAuthor != null && extractedGenre != null && extractedYear > 0) {
                Book book = new Book(generateUniqueId(), extractedTitle, extractedAuthor, extractedYear, extractedGenre);
                libraryManager.addBook(book);
            }

            return openAIResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to communicate with OpenAI API\"}";
        }
    }

    private String extractField(String response, String field) {
        Pattern pattern = Pattern.compile("\"" + field + "\":\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(response);
        return matcher.find() ? matcher.group(1) : null;
    }

    private int extractFieldAsInt(String response, String field, int defaultValue) {
        Pattern pattern = Pattern.compile("\"" + field + "\":\\s*(\\d+)");
        Matcher matcher = pattern.matcher(response);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : defaultValue;
    }

    private int generateUniqueId() {
        return (int) (Math.random() * 10000);
    }
}


