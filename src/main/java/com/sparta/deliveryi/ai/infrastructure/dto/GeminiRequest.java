package com.sparta.deliveryi.ai.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GeminiRequest(
    List<Content> contents,
    @JsonProperty("config")
    GenerationConfig generationConfig
) {
    public static GeminiRequest of(String prompt) {
        return new GeminiRequest(
            List.of(new Content(List.of(new Part(prompt)))),
            new GenerationConfig(0.7)
        );
    }
}

record Content(List<Part> parts) {}

record Part(String text) {}

record GenerationConfig(double temperature) {}