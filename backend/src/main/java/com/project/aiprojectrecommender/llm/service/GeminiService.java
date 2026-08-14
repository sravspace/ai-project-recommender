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

import java.util.LinkedHashMap;
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
                    ),

                    "generationConfig",
                    Map.of(
                            "responseMimeType", "application/json",
                            "responseSchema", buildResponseSchema()
                    )
            );

            String response = restClient.post()
                    .uri(url)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            log.info("Gemini raw response received successfully.");

            GeminiResponse geminiResponse =
                    objectMapper.readValue(
                            response,
                            GeminiResponse.class
                    );

            return geminiResponse
                    .getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();

        } catch (RestClientResponseException e) {

            log.error(
                    "Gemini API call failed. status={} body={}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );

            throw new RuntimeException(
                    "Gemini call failed. status="
                            + e.getStatusCode()
                            + " body="
                            + e.getResponseBodyAsString(),
                    e
            );

        } catch (Exception e) {

            log.error(
                    "Failed to generate Gemini recommendation.",
                    e
            );

            throw new RuntimeException(
                    "Failed to generate recommendations.",
                    e
            );
        }
    }

    private Map<String, Object> buildResponseSchema() {

        /*
         * Estimated time schema
         */
        Map<String, Object> estimatedTimeSchema =
                new LinkedHashMap<>();

        estimatedTimeSchema.put(
                "type",
                "object"
        );

        Map<String, Object> estimatedTimeProperties =
                new LinkedHashMap<>();

        estimatedTimeProperties.put(
                "learningHours",
                Map.of(
                        "type", "integer",
                        "minimum", 0
                )
        );

        estimatedTimeProperties.put(
                "buildHours",
                Map.of(
                        "type", "integer",
                        "minimum", 1
                )
        );

        estimatedTimeProperties.put(
                "totalWeeks",
                Map.of(
                        "type", "integer",
                        "minimum", 1,
                        "maximum", 6
                )
        );

        estimatedTimeSchema.put(
                "properties",
                estimatedTimeProperties
        );

        estimatedTimeSchema.put(
                "required",
                List.of(
                        "learningHours",
                        "buildHours",
                        "totalWeeks"
                )
        );


        /*
         * Project schema
         */
        Map<String, Object> projectSchema =
                new LinkedHashMap<>();

        projectSchema.put(
                "type",
                "object"
        );

        Map<String, Object> projectProperties =
                new LinkedHashMap<>();


        /*
         * title
         */
        projectProperties.put(
                "title",
                Map.of(
                        "type", "string"
                )
        );


        /*
         * description
         */
        projectProperties.put(
                "description",
                Map.of(
                        "type", "string"
                )
        );


        /*
         * difficulty
         */
        projectProperties.put(
                "difficulty",
                Map.of(
                        "type", "string",
                        "enum",
                        List.of(
                                "BEGINNER",
                                "INTERMEDIATE",
                                "ADVANCED"
                        )
                )
        );


        /*
         * isStretch
         */
        projectProperties.put(
                "isStretch",
                Map.of(
                        "type", "boolean"
                )
        );


        /*
         * estimatedTime
         */
        projectProperties.put(
                "estimatedTime",
                estimatedTimeSchema
        );


        /*
         * feasibilitySummary
         */
        projectProperties.put(
                "feasibilitySummary",
                Map.of(
                        "type", "string"
                )
        );


        /*
         * whyItFits
         */
        projectProperties.put(
                "whyItFits",
                Map.of(
                        "type", "array",
                        "items",
                        Map.of(
                                "type", "string"
                        ),
                        "minItems", 2,
                        "maxItems", 3
                )
        );


        /*
         * resumeSkills
         */
        projectProperties.put(
                "resumeSkills",
                Map.of(
                        "type", "array",
                        "items",
                        Map.of(
                                "type", "string"
                        ),
                        "minItems", 2,
                        "maxItems", 4
                )
        );


        /*
         * youWillLearn
         */
        projectProperties.put(
                "youWillLearn",
                Map.of(
                        "type", "array",
                        "items",
                        Map.of(
                                "type", "string"
                        ),
                        "minItems", 2,
                        "maxItems", 4
                )
        );


        /*
         * technologies
         */
        projectProperties.put(
                "technologies",
                Map.of(
                        "type", "array",
                        "items",
                        Map.of(
                                "type", "string"
                        ),
                        "minItems", 1
                )
        );


        /*
         * existingSkills
         */
        projectProperties.put(
                "existingSkills",
                Map.of(
                        "type", "array",
                        "items",
                        Map.of(
                                "type", "string"
                        ),
                        "minItems", 1
                )
        );


        /*
         * skillGaps
         */
        projectProperties.put(
                "skillGaps",
                Map.of(
                        "type", "array",
                        "items",
                        Map.of(
                                "type", "string"
                        )
                )
        );


        /*
         * prerequisites
         */
        projectProperties.put(
                "prerequisites",
                Map.of(
                        "type", "array",
                        "items",
                        Map.of(
                                "type", "string"
                        ),
                        "minItems", 1
                )
        );


        /*
         * stretchGoals
         */
        projectProperties.put(
                "stretchGoals",
                Map.of(
                        "type", "array",
                        "items",
                        Map.of(
                                "type", "string"
                        ),
                        "minItems", 1,
                        "maxItems", 2
                )
        );


        /*
         * Attach project properties
         */
        projectSchema.put(
                "properties",
                projectProperties
        );


        /*
         * Required project fields
         */
        projectSchema.put(
                "required",
                List.of(
                        "title",
                        "description",
                        "difficulty",
                        "isStretch",
                        "estimatedTime",
                        "feasibilitySummary",
                        "whyItFits",
                        "resumeSkills",
                        "youWillLearn",
                        "technologies",
                        "existingSkills",
                        "skillGaps",
                        "prerequisites",
                        "stretchGoals"
                )
        );


        /*
         * Root response schema
         */
        Map<String, Object> rootSchema =
                new LinkedHashMap<>();

        rootSchema.put(
                "type",
                "object"
        );

        Map<String, Object> rootProperties =
                new LinkedHashMap<>();

        rootProperties.put(
                "projects",
                Map.of(
                        "type", "array",
                        "minItems", 5,
                        "maxItems", 5,
                        "items", projectSchema
                )
        );

        rootSchema.put(
                "properties",
                rootProperties
        );

        rootSchema.put(
                "required",
                List.of(
                        "projects"
                )
        );

        return rootSchema;
    }
}