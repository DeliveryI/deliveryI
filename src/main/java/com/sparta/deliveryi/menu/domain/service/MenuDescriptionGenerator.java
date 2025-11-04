package com.sparta.deliveryi.menu.domain.service;

import com.sparta.deliveryi.ai.application.service.AiLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuDescriptionGenerator {

    private final AiLogService aiLogService;

    public Result generate(String prompt, String menuName, String menuDescription, boolean aiGenerate) {
        if (!aiGenerate) {
            return new Result(menuDescription, false, null);
        }

        String fullPrompt = aiLogService.buildFullPrompt(prompt, menuName);
        try {
            String description = aiLogService.callGeminiApiOnly(fullPrompt);
            return new Result(description, true, fullPrompt);
        } catch (Exception e) {
            return new Result("AI 호출 실패: " + e.getMessage(), false, fullPrompt);
        }
    }

    public record Result(String description, boolean aiGenerated, String fullPrompt) {}
}
