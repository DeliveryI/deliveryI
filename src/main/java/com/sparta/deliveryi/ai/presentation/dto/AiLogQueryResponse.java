package com.sparta.deliveryi.ai.presentation.dto;

import com.sparta.deliveryi.ai.domain.AiStatus;

public record AiLogQueryResponse(
        Long aiId,
        Long menuId,
        String prompt,
        String fullPrompt,
        String response,
        AiStatus aiStatus,
        String createdBy,
        String createdAt
) {}
