package com.sparta.deliveryi.ai.infrastructure.dto;

public record AiLogRequest(
        Long menuId,
        String prompt
) {
}