package com.sparta.deliveryi.ai.domain;

import com.sparta.deliveryi.ai.domain.exception.*;
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

    @Lob
    @Column(name = "prompt", nullable = false)
    private String prompt;

    @Lob
    @Column(name ="full_prompt", nullable = false)
    private String fullPrompt;

    @Lob
    @Column(name = "response", nullable = false)
    private String response;

    public static AiLog create(Long menuId, String prompt, String fullPrompt, String response, AiStatus aiStatus, String createdBy) {
        validatePromptNotEmpty(prompt);
        validateResponseNotEmpty(response);
        validateCreatedBy(createdBy);

        AiLog aiLog = new AiLog();
        aiLog.menuId = menuId;
        aiLog.prompt = prompt;
        aiLog.fullPrompt = fullPrompt;
        aiLog.response = response;
        aiLog.aiStatus = aiStatus != null ? aiStatus : AiStatus.SUCCESS;
        aiLog.createBy(createdBy);
        return aiLog;
    }

    private static void validatePromptNotEmpty(String prompt) {
        if (prompt == null || prompt.isBlank()) throw new AiPromptEmptyException();
    }

    private static void validateResponseNotEmpty(String response) {
        if (response == null || response.isBlank()) throw new AiResponseEmptyException();
    }

    private static void validateCreatedBy(String createdBy) {
        if (createdBy == null || createdBy.isBlank()) throw new AiCreatedByEmptyException();
    }

    @Override
    public void delete() {
        throw new AiLogDeletionNotAllowedException();
    }
}
