package com.sparta.deliveryi.ai.presentation.dto;

public record AiLogRequest(
        Long menuId,
        String prompt
) {
}