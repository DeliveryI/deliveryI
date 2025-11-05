package com.sparta.deliveryi.ai.domain;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import com.sparta.deliveryi.ai.domain.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AiLog 도메인 단위 테스트")
class AiLogTest {

    @BeforeEach
    void setUp() {
        TestMessageResolverInitializer.initializeFromResourceBundle();
    }

    @Test
    @DisplayName("정상적으로 AI 로그 생성 성공")
    void create_success() {
        AiLog aiLog = AiLog.create(1L, "프롬프트", "전체프롬프트", "응답본문", AiStatus.SUCCESS, "tester");

        assertThat(aiLog.getMenuId()).isEqualTo(1L);
        assertThat(aiLog.getPrompt()).isEqualTo("프롬프트");
        assertThat(aiLog.getResponse()).isEqualTo("응답본문");
        assertThat(aiLog.getCreatedBy()).isEqualTo("tester");
        assertThat(aiLog.getAiStatus()).isEqualTo(AiStatus.SUCCESS);
    }

    @Test
    @DisplayName("prompt가 비어있으면 예외 발생")
    void create_emptyPrompt() {
        assertThatThrownBy(() ->
                AiLog.create(1L, "", "전체", "응답", AiStatus.SUCCESS, "tester")
        ).isInstanceOf(AiPromptEmptyException.class);
    }

    @Test
    @DisplayName("response가 비어있으면 예외 발생")
    void create_emptyResponse() {
        assertThatThrownBy(() ->
                AiLog.create(1L, "프롬프트", "전체", "", AiStatus.SUCCESS, "tester")
        ).isInstanceOf(AiResponseEmptyException.class);
    }

    @Test
    @DisplayName("createdBy가 비어있으면 예외 발생")
    void create_emptyCreatedBy() {
        assertThatThrownBy(() ->
                AiLog.create(1L, "프롬프트", "전체", "응답", AiStatus.SUCCESS, " ")
        ).isInstanceOf(AiCreatedByEmptyException.class);
    }

    @Test
    @DisplayName("AI 로그는 삭제할 수 없음")
    void delete_forbidden() {
        AiLog aiLog = AiLog.create(1L, "프롬프트", "전체", "응답", AiStatus.SUCCESS, "tester");

        assertThatThrownBy(aiLog::delete)
                .isInstanceOf(AiLogDeletionNotAllowedException.class);
    }
}
