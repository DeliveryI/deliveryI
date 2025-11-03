package com.sparta.deliveryi.ai.presentation;

public record AiLogRequest(
        Long menuId,
        String prompt
) {
}