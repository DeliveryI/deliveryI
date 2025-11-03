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

    private static final String PREFIX = "너는 지금 음식점 마케터야. 다른 설명없이 다음 질문에 대한 답만 해줘.";
    private static final String SUFFIX = "설명을 더 잘 팔릴 수 있게 상품 설명을 70~100글자 안으로 3가지 작성해줘. 3가지는 _-_-_로 구분해 ";

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

    public static String appendPrefixAndSuffix(String prompt) {
        return PREFIX + " " + prompt + " " + SUFFIX;
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
