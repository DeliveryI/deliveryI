package com.sparta.deliveryi.ai.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.exception.AiCallFailedException;
import com.sparta.deliveryi.ai.infrastructure.dto.GeminiRequest;
import com.sparta.deliveryi.ai.infrastructure.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AiLogService {

    private static final String CONTENT_TYPE = "application/json";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${google.ai.api-key}")
    private String geminiApiKey;

    @Value("${google.ai.endpoint}")
    private String geminiEndpoint;

    // prompt가 없으면 menuName 사용 + prefix/suffix 추가
    public String buildFullPrompt(String prompt, String menuName) {
        String basePrompt = (prompt == null || prompt.isBlank()) ? menuName : prompt;
        return AiLog.appendPrefixAndSuffix(basePrompt);
    }

    // Gemini API 호출
    private String callGeminiApi(String prompt) throws Exception {
        String fullUri = geminiEndpoint + "?key=" + geminiApiKey;
        String requestBody = objectMapper.writeValueAsString(GeminiRequest.of(prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUri))
                .header("Content-Type", CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            log.warn("Gemini API 응답 오류 - status: {}", response.statusCode());
            throw new AiCallFailedException();
        }

        return extractTextFromGeminiResponse(response.body());
    }

    // Gemini 응답 본문 파싱
    private String extractTextFromGeminiResponse(String jsonResponse) throws Exception {
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

    // 예외를 던지지 않고 단순 호출 결과만 반환하는 헬퍼
    public String callGeminiApiOnly(String fullPrompt) {
        try {
            return callGeminiApi(fullPrompt);
        } catch (Exception e) {
            throw new AiCallFailedException();
        }
    }

    // 현재 로그인 사용자명 조회
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName()
                : "anonymousUser";
    }

    // 내부 응답 파싱용 record
    private record DescriptionWrapper(String description) {}
}
