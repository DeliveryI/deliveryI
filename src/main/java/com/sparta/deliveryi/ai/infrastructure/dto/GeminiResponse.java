package com.sparta.deliveryi.ai.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GeminiResponse(
    List<Candidate> candidates
) {

    public String extractText() {
        if (candidates() != null && !candidates().isEmpty()) {
            Candidate candidate = candidates().getFirst();
            if (candidate.content() != null && candidate.content().parts() != null && !candidate.content().parts().isEmpty()) {
                return candidate.content().parts().getFirst().text();
            }
        }
        return "AI 응답 파싱 실패";
    }
}

record Candidate(
    Content content,
    @JsonProperty("finishReason") 
    String finishReason
) {}