package com.sparta.deliveryi.menu.domain.service;

import com.sparta.deliveryi.ai.application.service.AiLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuDescriptionGenerator {

    private static final String PREFIX = "너는 지금 음식점 마케터야. 다른 설명없이 다음 질문에 대한 답만 해줘.";
    private static final String SUFFIX = "설명을 더 잘 팔릴 수 있게, 상품 설명을 45~60자 정도 길이로 작성해줘.";

    private final AiLogService aiLogService;

    public Result generate(String prompt, String menuName, String menuDescription, boolean aiGenerate) {
        if (!aiGenerate) {
            return new Result(menuDescription, false, null);
        }

        String fullPrompt = buildFullPrompt(prompt, menuName);

        try {
            String description = aiLogService.callGeminiApiOnly(fullPrompt);
            return new Result(description, true, fullPrompt);
        } catch (Exception e) {
            return new Result("AI 호출 실패: " + e.getMessage(), false, fullPrompt);
        }
    }

    private String buildFullPrompt(String prompt, String menuName) {
        String target = (prompt != null && !prompt.isBlank()) ? prompt : menuName;
        return PREFIX + " " + target + " " + SUFFIX;
    }

    public record Result(String description, boolean aiGenerated, String fullPrompt) {}
}
