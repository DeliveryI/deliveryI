package com.sparta.deliveryi.ai.presentation.webapi;

import com.sparta.deliveryi.ai.application.service.AiLogQueryService;
import com.sparta.deliveryi.ai.presentation.dto.AiLogQueryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiLogQueryApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AiLogQueryApi 테스트")
class AiLogQueryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AiLogQueryService aiLogQueryService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        AiLogQueryService aiLogQueryService() {
            return Mockito.mock(AiLogQueryService.class);
        }
    }

    @Test
    @DisplayName("GET /v1/ai/logs/{menuId} 호출 시 성공 응답 반환")
    void getAiLogsByMenu_success() throws Exception {
        // given
        Long menuId = 1L;

        AiLogQueryResponse mockResponse = new AiLogQueryResponse(
                1L, menuId, "prompt", "fullPrompt", "response",
                "SUCCESS", "tester", "2025-11-07T10:00:00"
        );

        Page<AiLogQueryResponse> mockPage = new PageImpl<>(List.of(mockResponse));

        BDDMockito.given(aiLogQueryService.getAiLogsByMenu(eq(menuId), any(Pageable.class)))
                .willReturn(mockPage);

        // when & then
        mockMvc.perform(get("/v1/ai/logs/{menuId}", menuId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].menuId").value(menuId))
                .andExpect(jsonPath("$.data.content[0].aiStatus").value("SUCCESS"));
    }
}
