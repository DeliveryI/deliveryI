package com.sparta.deliveryi.ai.infrastructure.dto;

import java.util.List;
import java.util.Map;

public record GeminiRequest(
        List<Content> contents,
        GenerationConfig generationConfig
) {
    public static GeminiRequest of(String prompt) {
        return new GeminiRequest(
                List.of(new Content(List.of(new Part(prompt)))),
                new GenerationConfig(
                        0.5,
                        "text/plain",
                        null,
                        1
                )
        );
    }
}

record Content(List<Part> parts) {}
record Part(String text) {}

record GenerationConfig(
        double temperature,
        String responseMimeType,
        ResponseSchema responseSchema,
        Integer candidateCount
) {}

record ResponseSchema(String type, SchemaItem items) {}

record SchemaItem(String type, Map<String, Map<String, Object>> properties) {}

