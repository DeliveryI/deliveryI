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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        // AI 호출용 프롬프트
        String fullPrompt = AiLog.appendPrefixAndSuffix(requestDto.prompt());

        // Gemini API 호출
        String response;
        AiStatus status = AiStatus.SUCCESS;
        try {
            response = callGeminiApi(fullPrompt);
        } catch (Exception e) {
            System.err.println("Gemini API 호출 실패: " + e.getMessage());
            response = "API 호출 실패: " + e.getMessage();
            status = AiStatus.FAILED;
        }

        // 로그인 사용자 가져오기 (Spring Security 없으면 anonymousUser)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName()
                : "anonymousUser";

        // DB 저장
        AiLog aiLog = AiLog.create(
                requestDto.menuId(),
                requestDto.prompt(),
                fullPrompt,
                response,
                status,
                currentUsername
        );

        AiLog savedLog = aiLogRepository.save(aiLog);

        // 응답 반환
        return new AiLogResponse(
                savedLog.getAiId(),
                savedLog.getMenuId(),
                savedLog.getPrompt(),
                savedLog.getFullPrompt(),
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

    private String createRequestBody(String prompt) throws Exception {
        GeminiRequest geminiRequest = GeminiRequest.of(prompt);
        return objectMapper.writeValueAsString(geminiRequest);
    }

    private String extractTextFromGeminiResponse(String jsonResponse) throws Exception {
        GeminiResponse geminiResponse = objectMapper.readValue(jsonResponse, GeminiResponse.class);

        String combinedText = geminiResponse.candidates().stream()
                .flatMap(c -> c.content().parts().stream())
                .map(GeminiResponse.Part::text)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));

        if (combinedText.trim().startsWith("[") || combinedText.trim().startsWith("{")) {
            try {
                List<DescriptionWrapper> descriptions = objectMapper.readValue(
                        combinedText,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, DescriptionWrapper.class)
                );
                return descriptions.stream()
                        .map(DescriptionWrapper::description)
                        .collect(Collectors.joining("\n"));
            } catch (Exception e) {
                return combinedText;
            }
        }

        return combinedText;
    }

    record DescriptionWrapper(String description) {}
}
