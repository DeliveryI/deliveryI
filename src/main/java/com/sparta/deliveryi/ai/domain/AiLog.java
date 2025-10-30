package com.sparta.deliveryi.ai.domain;

import com.sparta.deliveryi.ai.domain.exception.AiCreatedByEmptyException;
import com.sparta.deliveryi.ai.domain.exception.AiLogDeletionNotAllowedException;
import com.sparta.deliveryi.ai.domain.exception.AiPromptEmptyException;
import com.sparta.deliveryi.ai.domain.exception.AiResponseEmptyException;
import com.sparta.deliveryi.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_ai")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiLog extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_id")
    private Long aiId;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_status", nullable = false)
    private AiStatus aiStatus;

    @Column(name = "prompt", nullable = false, length = 255)
    private String prompt;

    @Column(name = "response", nullable = false, length = 255)
    private String response;

    // 생성 메서드
    public static AiLog create(Long menuId, String prompt, String response, AiStatus aiStatus, String createdBy) {
        validatePrompt(prompt);
        validateResponse(response);
        validateCreatedBy(createdBy);

        AiLog log = new AiLog();
        log.menuId = menuId;
        log.prompt = appendLimitText(prompt);
        log.response = response;
        log.aiStatus = aiStatus != null ? aiStatus : AiStatus.SUCCESS;
        log.createBy(createdBy);
        return log;
    }

    private static void validatePrompt(String prompt) {
        if (prompt == null || prompt.isBlank()) throw new AiPromptEmptyException();
    }

    private static void validateResponse(String response) {
        if (response == null || response.isBlank()) throw new AiResponseEmptyException();
    }

    private static void validateCreatedBy(String createdBy) {
        if (createdBy == null || createdBy.isBlank()) throw new AiCreatedByEmptyException();
    }

    private static String appendLimitText(String prompt) {
        String suffix = " 답변을 최대한 간결하게 50자 이하로";
        int maxLength = 255 - suffix.length();
        String trimmed = (prompt.length() > maxLength) ? prompt.substring(0, maxLength) : prompt;
        return trimmed + suffix;
    }

    @Override
    public void delete() {
        throw new AiLogDeletionNotAllowedException();
    }
}
