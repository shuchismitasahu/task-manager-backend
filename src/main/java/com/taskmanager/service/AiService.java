package com.taskmanager.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.AiTaskResponse;

@Service
public class AiService {

	@Value("${ai.gemini.api-key}")
	private String apiKey;

	@Value("${ai.gemini.api-url}")
	private String apiUrl;

	private final HttpClient httpClient = HttpClient.newHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public AiTaskResponse generateTaskDetails(String title) {
		try {
			String url = apiUrl + "?key=" + apiKey;

			String requestBody = """
					{
					    "contents": [{
					        "parts": [{
					            "text": "You are a task management assistant. Given this task title: '%s', respond ONLY with a valid JSON object containing these fields: description (string), priority (LOW or MEDIUM or HIGH), estimatedTime (string like 2 hours). No extra text, no markdown, no code blocks."
					        }]
					    }]
					}
					"""
					.formatted(escapeJson(title));

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			return parseGeminiResponse(response.body());

		} catch (Exception e) {
			return getFallbackResponse(title);
		}
	}

	// Parse Gemini API response format
	private AiTaskResponse parseGeminiResponse(String responseBody) {
		try {
			JsonNode root = objectMapper.readTree(responseBody);

			// Gemini response path
			String content = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

			// Remove markdown code blocks if present
			content = content.replaceAll("```json", "").replaceAll("```", "").trim();

			// Parse the JSON from AI
			JsonNode aiJson = objectMapper.readTree(content);

			AiTaskResponse aiResponse = new AiTaskResponse();
			aiResponse.setDescription(aiJson.path("description").asText());
			aiResponse.setPriority(aiJson.path("priority").asText("MEDIUM"));
			aiResponse.setEstimatedTime(aiJson.path("estimatedTime").asText("2 hours"));
			aiResponse.setAiGenerated(true);
			return aiResponse;

		} catch (Exception e) {
			return getFallbackResponse("task");
		}
	}

	// Fallback if AI fails
	private AiTaskResponse getFallbackResponse(String title) {
		AiTaskResponse fallback = new AiTaskResponse();
		fallback.setDescription("Please add a description for: " + title);
		fallback.setPriority("MEDIUM");
		fallback.setEstimatedTime("1-2 hours");
		fallback.setAiGenerated(false);
		return fallback;
	}

	// Escape special characters for JSON
	private String escapeJson(String text) {
		return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
	}
}