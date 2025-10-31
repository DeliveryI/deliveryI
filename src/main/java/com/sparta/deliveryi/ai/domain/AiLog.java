package com.sparta.deliveryi.ai.domain;

import com.sparta.deliveryi.ai.domain.exception.*;
import com.sparta.deliveryi.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.util.Assert.state;
import java.nio.charset.StandardCharsets;

@Entity
@Table(name = "p_ai")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiLog extends AbstractEntity {

    private static final int PROMPT_MAX_BYTES = 255;
    private static final String SUFFIX = " 답변을 최대한 간결하게 50자 이하로";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_id")
    private Long aiId;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_status", nullable = false)
    private AiStatus aiStatus;

    @Column(name = "prompt", nullable = false)
    private String prompt;

    @Column(name = "response", nullable = false)
    private String response;

    // 생성 메서드
    public static AiLog create(Long menuId, String prompt, String response, AiStatus aiStatus, String createdBy) {
        validatePromptNotEmpty(prompt);
        validatePromptLength(prompt);
        validateResponseNotEmpty(response);
        validateCreatedBy(createdBy);

        AiLog log = new AiLog();
        log.menuId = menuId;
        log.prompt = appendSuffix(prompt);
        log.response = response;
        log.aiStatus = aiStatus != null ? aiStatus : AiStatus.SUCCESS;
        log.createBy(createdBy);
        return log;
    }

    private static void validatePromptNotEmpty(String prompt) {
        if (prompt == null || prompt.isBlank()) throw new AiPromptEmptyException();
    }

    private static void validatePromptLength(String prompt) {
        int promptBytes = prompt.getBytes(StandardCharsets.UTF_8).length;
        int suffixBytes = SUFFIX.getBytes(StandardCharsets.UTF_8).length;

        state(promptBytes + suffixBytes <= PROMPT_MAX_BYTES,
                "AI 요청 프롬프트가 너무 깁니다. 최대 " + (PROMPT_MAX_BYTES - suffixBytes) + "바이트까지 가능합니다.");
    }

    private static void validateResponseNotEmpty(String response) {
        if (response == null || response.isBlank()) throw new AiResponseEmptyException();
    }

    private static void validateCreatedBy(String createdBy) {
        if (createdBy == null || createdBy.isBlank()) throw new AiCreatedByEmptyException();
    }

    private static String appendSuffix(String prompt) {
        return prompt + SUFFIX;
    }

    @Override
    public void delete() {
        throw new AiLogDeletionNotAllowedException();
    }
}
