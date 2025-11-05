package com.sparta.deliveryi.ai.application.service;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.exception.AiResponseEmptyException;
import com.sparta.deliveryi.ai.domain.service.AiClient;
import com.sparta.deliveryi.ai.domain.service.AiLogRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("AiApplicationService 단위 테스트")
class AiApplicationServiceTest {

    @BeforeEach
    void setUp() {
        TestMessageResolverInitializer.initializeFromResourceBundle();
    }
    @Mock
    private AiClient aiClient;

    @Mock
    private AiLogRegister aiLogRegister;

    @InjectMocks
    private AiApplicationService aiApplicationService;

    @Test
    @DisplayName("AI 생성 비활성화 시 - AI 호출과 로그 저장 없이 종료")
    void generate_aiDisabled_success() {
        // given
        Long menuId = 1L;
        String username = "tester";
        String prompt = "테스트 프롬프트";

        // when
        AiApplicationService.AiResult result = aiApplicationService.generate(
                menuId, "김치찌개", prompt, false, username
        );

        // then
        assertThat(result.description()).isNull();
        assertThat(result.aiGenerated()).isFalse();
        assertThat(result.fullPrompt()).isEqualTo(prompt);

        verify(aiClient, never()).requestDescription(anyString());
        verify(aiLogRegister, never()).save(any(AiLog.class));
    }

    @Test
    @DisplayName("AI 생성 활성화 시 - AI 호출 및 로그 저장 성공")
    void generate_aiEnabled_success() {
        // given
        Long menuId = 1L;
        String username = "tester";
        String prompt = "맛있는 전복죽 설명을 생성해줘";
        String aiResponse = "영양 가득 전복죽! 건강한 한 그릇으로 활력 충전.";

        given(aiClient.requestDescription(prompt)).willReturn(aiResponse);

        // when
        AiApplicationService.AiResult result = aiApplicationService.generate(
                menuId, "전복죽", prompt, true, username
        );

        // then
        assertThat(result.description()).isEqualTo(aiResponse);
        assertThat(result.aiGenerated()).isTrue();
        assertThat(result.fullPrompt()).isEqualTo(prompt);

        verify(aiClient).requestDescription(prompt);
        verify(aiLogRegister).save(any(AiLog.class));
    }

    @Test
    @DisplayName("AI 호출 결과가 null이면 예외 발생 (AiResponseEmptyException)")
    void generate_aiEnabled_withNullResponse_shouldThrowException() {
        // given
        Long menuId = 2L;
        String username = "tester";
        String prompt = "AI 응답 테스트";
        given(aiClient.requestDescription(prompt)).willReturn(null);

        // when & then
        assertThatThrownBy(() ->
                aiApplicationService.generate(menuId, "불고기", prompt, true, username)
        )
                .isInstanceOf(AiResponseEmptyException.class)
                .hasMessage("AI 응답은 반드시 입력되어야 합니다.");

        verify(aiClient).requestDescription(prompt);
        verify(aiLogRegister, never()).save(any());
    }

}
