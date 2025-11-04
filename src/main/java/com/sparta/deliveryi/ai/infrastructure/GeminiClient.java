package com.sparta.deliveryi.ai.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.ai.domain.exception.AiCallFailedException;
import com.sparta.deliveryi.ai.infrastructure.dto.GeminiRequest;
import com.sparta.deliveryi.ai.infrastructure.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    private static final String CONTENT_TYPE = "application/json";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${google.ai.api-key}")
    private String geminiApiKey;

    @Value("${google.ai.endpoint}")
    private String geminiEndpoint;

    /** Gemini API 호출 */
    public String requestDescription(String prompt) {
        try {
            String uri = geminiEndpoint + "?key=" + geminiApiKey;
            String requestBody = objectMapper.writeValueAsString(GeminiRequest.of(prompt));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", CONTENT_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("Gemini API 응답 오류 - status: {}", response.statusCode());
                throw new AiCallFailedException();
            }

            return extractTextFromResponse(response.body());

        } catch (Exception e) {
            throw new AiCallFailedException();
        }
    }

    /** Gemini 응답 파싱 */
    private String extractTextFromResponse(String jsonResponse) throws Exception {
        GeminiResponse geminiResponse = objectMapper.readValue(jsonResponse, GeminiResponse.class);

        String combinedText = geminiResponse.candidates().stream()
                .flatMap(c -> c.content().parts().stream())
                .map(GeminiResponse.Part::text)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"))
                .trim();

        if (combinedText.startsWith("[") || combinedText.startsWith("{")) {
            try {
                List<DescriptionWrapper> descriptions = objectMapper.readValue(
                        combinedText,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, DescriptionWrapper.class)
                );
                return descriptions.stream()
                        .map(DescriptionWrapper::description)
                        .collect(Collectors.joining("\n"));
            } catch (Exception e) {
                log.debug("JSON 파싱 실패, 원문 반환");
                return combinedText;
            }
        }
        return combinedText;
    }

    private record DescriptionWrapper(String description) {}
}
