package com.sparta.deliveryi.ai.presentation.dto;

import com.sparta.deliveryi.ai.domain.AiLog;

public record AiLogQueryResponse(
        Long aiId,
        Long menuId,
        String prompt,
        String fullPrompt,
        String response,
        String aiStatus,
        String createdBy,
        String createdAt
) {

    public static AiLogQueryResponse from(AiLog aiLog) {
        return new AiLogQueryResponse(
                aiLog.getAiId(),
                aiLog.getMenuId(),
                aiLog.getPrompt(),
                aiLog.getFullPrompt(),
                aiLog.getResponse(),
                aiLog.getAiStatus().name(),
                aiLog.getCreatedBy(),
                aiLog.getCreatedAt().toString()
        );
    }
}
