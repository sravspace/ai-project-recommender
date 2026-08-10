package com.project.aiprojectrecommender.llm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aiprojectrecommender.recommendation.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String MODEL = "gemini-3.6-flash";

    public String generate(String prompt) {
        try {
            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/"
                            + MODEL
                            + ":generateContent";

            Map<String, Object> request = Map.of(
                    "contents",
                    List.of(
                            Map.of(
                                    "role", "user",
                                    "parts",
                                    List.of(
                                            Map.of(
                                                    "text", prompt
                                            )
                                    )
                            )
                    )
            );

            String response = restClient.post()
                    .uri(url)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            GeminiResponse geminiResponse =
                    objectMapper.readValue(response, GeminiResponse.class);

            return geminiResponse.getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();

        } catch (RestClientResponseException e) {
            // This is the block that was missing: Google returns a structured
            // JSON error body explaining exactly why auth/permission failed.
            // Your old catch(Exception) discarded it before you ever saw it.
            log.error("Gemini API call failed. status={} body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException(
                    "Gemini call failed: status=" + e.getStatusCode()
                            + " body=" + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate recommendations.", e);
        }
    }
}