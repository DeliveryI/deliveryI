package com.sparta.deliveryi.ai.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.service.AiLogRepository;
import com.sparta.deliveryi.ai.domain.AiStatus;
import com.sparta.deliveryi.ai.infrastructure.dto.AiLogRequest;
import com.sparta.deliveryi.ai.infrastructure.dto.AiLogResponse;
import com.sparta.deliveryi.ai.infrastructure.dto.GeminiRequest;
import com.sparta.deliveryi.ai.infrastructure.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class AiLogService {

    private final AiLogRepository aiLogRepository;
    private final ObjectMapper objectMapper;

    @Value("${google.ai.api-key}")
    private String geminiApiKey;

    @Value("${google.ai.endpoint}")
    private String geminiEndpoint;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Transactional
    public AiLogResponse createAiLog(AiLogRequest requestDto) {
        String prompt = requestDto.prompt();

        String response;
        try {
            response = callGeminiApi(prompt);
        } catch (Exception e) {
            System.err.println("Gemini API 호출 실패: " + e.getMessage());
            response = "API 호출 실패: " + e.getMessage();
        }

        AiLog aiLog = AiLog.create(
                requestDto.menuId(),
                prompt,
                response,
                AiStatus.SUCCESS,
                requestDto.createdBy()
        );

        AiLog savedLog = aiLogRepository.save(aiLog);

        return new AiLogResponse(
                savedLog.getAiId(),
                savedLog.getMenuId(),
                savedLog.getPrompt(),
                savedLog.getResponse(),
                savedLog.getAiStatus(),
                savedLog.getCreatedBy()
        );
    }

    private String callGeminiApi(String prompt) throws Exception {
        String requestBody = createRequestBody(prompt);

        String fullUri = this.geminiEndpoint + "?key=" + this.geminiApiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return extractTextFromGeminiResponse(response.body());
        } else {
            throw new RuntimeException("API 호출 실패. 상태 코드: " + response.statusCode() + ", 본문: " + response.body());
        }
    }

    /**
     * Gemini API 요청에 사용할 JSON 본문을 생성합니다.
     */
    private String createRequestBody(String prompt) throws Exception {
        GeminiRequest geminiRequest = GeminiRequest.of(prompt);
        return objectMapper.writeValueAsString(geminiRequest);
    }

    /**
     * Gemini API 응답 JSON에서 최종 텍스트만 추출합니다. (Jackson 사용)
     */
    private String extractTextFromGeminiResponse(String jsonResponse) throws Exception {
        GeminiResponse geminiResponse = objectMapper.readValue(jsonResponse, GeminiResponse.class);

        return geminiResponse.extractText();
    }
}
