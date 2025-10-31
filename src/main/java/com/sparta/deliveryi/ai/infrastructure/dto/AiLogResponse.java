package com.sparta.deliveryi.ai.infrastructure.dto;

import com.sparta.deliveryi.ai.domain.AiStatus;

public record AiLogResponse(
        Long aiId,
        Long menuId,
        String prompt,
        String response,
        AiStatus aiStatus,
        String createdBy
) {
}