package com.taskmanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.AiTaskResponse;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AiService {

    @Value("${ai.openai.api-key}")
    private String openAiApiKey;

    @Value("${ai.openai.api-url}")
    private String openAiApiUrl;

    @Value("${ai.openai.model}")
    private String model;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Generate task details using AI given a task title
    public AiTaskResponse generateTaskDetails(String title) {
        try {
            // Build the prompt for OpenAI
            String prompt = buildPrompt(title);

            // Build request body JSON
            String requestBody = """
                    {
                        "model": "%s",
                        "messages": [
                            {
                                "role": "system",
                                "content": "You are a helpful task management assistant. When given a task title, respond ONLY with a JSON object containing: description (string), priority (LOW, MEDIUM, or HIGH), estimatedTime (string like '2 hours' or '30 minutes'). No extra text."
                            },
                            {
                                "role": "user",
                                "content": "%s"
                            }
                        ],
                        "max_tokens": 300,
                        "temperature": 0.7
                    }
                    """.formatted(model, escapeJson(prompt));

            // Make HTTP request to OpenAI
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(openAiApiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // Parse and return the AI response
            return parseAiResponse(response.body());

        } catch (Exception e) {
            // Graceful fallback if AI service fails
            return getFallbackResponse(title);
        }
    }

    // Build the user prompt
    private String buildPrompt(String title) {
        return "Generate task details for: " + title;
    }

    // Parse OpenAI response JSON
    private AiTaskResponse parseAiResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // Remove markdown code blocks if present
            content = content.replaceAll("```json", "").replaceAll("```", "").trim();

            // Parse the AI's JSON response
            JsonNode aiJson = objectMapper.readTree(content);

            AiTaskResponse aiResponse = new AiTaskResponse();
            aiResponse.setDescription(aiJson.path("description").asText());
            aiResponse.setPriority(aiJson.path("priority").asText("MEDIUM"));
            aiResponse.setEstimatedTime(aiJson.path("estimatedTime").asText("2 hours"));
            aiResponse.setAiGenerated(true);
            return aiResponse;

        } catch (Exception e) {
            // If parsing fails, return a default fallback
            return getFallbackResponse("task");
        }
    }

    // Fallback response if AI service is unavailable
    private AiTaskResponse getFallbackResponse(String title) {
        AiTaskResponse fallback = new AiTaskResponse();
        fallback.setDescription("Please add a description for: " + title);
        fallback.setPriority("MEDIUM");
        fallback.setEstimatedTime("1-2 hours");
        fallback.setAiGenerated(false);
        return fallback;
    }

    // Escape special characters for JSON string
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }
}
