package com.sparta.deliveryi.ai.application.service;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.AiStatus;
import com.sparta.deliveryi.ai.domain.exception.AiMenuNotFoundException;
import com.sparta.deliveryi.ai.domain.service.AiLogFinder;
import com.sparta.deliveryi.ai.domain.service.MenuLookupClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("AiLogQueryService 단위 테스트")
class AiLogQueryServiceTest {

    @Mock
    private AiLogFinder aiLogFinder;

    @Mock
    private MenuLookupClient menuLookupClient;

    @InjectMocks
    private AiLogQueryService aiLogQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TestMessageResolverInitializer.initializeFromResourceBundle();
    }

    @Test
    @DisplayName("존재하는 메뉴 ID로 AI 로그 조회 성공")
    void getAiLogsByMenu_success() {
        // given
        Long menuId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        AiLog mockLog = mock(AiLog.class);
        when(mockLog.getAiId()).thenReturn(1L);
        when(mockLog.getMenuId()).thenReturn(menuId);
        when(mockLog.getPrompt()).thenReturn("테스트 프롬프트");
        when(mockLog.getFullPrompt()).thenReturn("테스트 풀프롬프트");
        when(mockLog.getResponse()).thenReturn("테스트 응답");
        when(mockLog.getAiStatus()).thenReturn(AiStatus.SUCCESS);
        when(mockLog.getCreatedBy()).thenReturn("tester");
        when(mockLog.getCreatedAt()).thenReturn(java.time.LocalDateTime.now());

        Page<AiLog> mockPage = new PageImpl<>(List.of(mockLog));

        given(menuLookupClient.existsMenuById(menuId)).willReturn(true);
        given(aiLogFinder.findAllByMenuId(eq(menuId), any(Pageable.class))).willReturn(mockPage);

        // when
        Page<AiLog> result = aiLogQueryService.getAiLogsByMenu(menuId, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getAiStatus()).isEqualTo(AiStatus.SUCCESS);
        verify(menuLookupClient).existsMenuById(menuId);
        verify(aiLogFinder).findAllByMenuId(eq(menuId), any(Pageable.class));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 ID로 조회 시 AiMenuNotFoundException 발생")
    void getAiLogsByMenu_notFound() {
        // given
        Long menuId = 999L;
        Pageable pageable = PageRequest.of(0, 10);
        given(menuLookupClient.existsMenuById(menuId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> aiLogQueryService.getAiLogsByMenu(menuId, pageable))
                .isInstanceOf(AiMenuNotFoundException.class);

        verify(menuLookupClient).existsMenuById(menuId);
        verify(aiLogFinder, never()).findAllByMenuId(anyLong(), any());
    }

    @Test
    @DisplayName("허용되지 않은 페이지 크기일 경우 기본값(10)으로 조정")
    void adjustPageSize_invalidSize_defaultsTo10() {
        // given
        Long menuId = 1L;
        Pageable pageable = PageRequest.of(0, 15); // 허용되지 않은 크기
        Page<AiLog> mockPage = new PageImpl<>(List.of());
        given(menuLookupClient.existsMenuById(menuId)).willReturn(true);
        given(aiLogFinder.findAllByMenuId(eq(menuId), any())).willReturn(mockPage);

        // when
        aiLogQueryService.getAiLogsByMenu(menuId, pageable);

        // then
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(aiLogFinder).findAllByMenuId(eq(menuId), pageableCaptor.capture());

        Pageable validatedPageable = pageableCaptor.getValue();
        assertThat(validatedPageable.getPageSize()).isEqualTo(10); // DEFAULT_PAGE_SIZE
    }

    @Test
    @DisplayName("허용된 페이지 크기(10, 30, 50)는 그대로 유지됨")
    void adjustPageSize_validSizes_staysSame() {
        // given
        Long menuId = 1L;
        given(menuLookupClient.existsMenuById(menuId)).willReturn(true);
        given(aiLogFinder.findAllByMenuId(eq(menuId), any())).willReturn(Page.empty());

        List<Integer> allowedSizes = List.of(10, 30, 50);

        // when & then
        for (int size : allowedSizes) {
            Pageable pageable = PageRequest.of(0, size);
            aiLogQueryService.getAiLogsByMenu(menuId, pageable);

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(aiLogFinder, atLeastOnce()).findAllByMenuId(eq(menuId), pageableCaptor.capture());
            assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(size);
        }
    }
}
