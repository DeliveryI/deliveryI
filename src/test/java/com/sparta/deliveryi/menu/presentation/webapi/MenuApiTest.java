package com.sparta.deliveryi.menu.presentation.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.menu.application.dto.MenuCommand;
import com.sparta.deliveryi.menu.application.dto.MenuResult;
import com.sparta.deliveryi.menu.application.service.MenuService;
import com.sparta.deliveryi.menu.domain.MenuStatus;
import com.sparta.deliveryi.menu.presentation.dto.MenuRequest;
import com.sparta.deliveryi.menu.presentation.dto.MenuStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuApi.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MenuApiTest.TestConfig.class)
@DisplayName("MenuApi 테스트")
class MenuApiTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired MenuService menuService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        MenuService menuService() {
            return Mockito.mock(MenuService.class);
        }
    }


    @Test
    @DisplayName("POST /v1/menus - 메뉴 등록 성공")
    void createMenu_success() throws Exception {
        UUID storeId = UUID.randomUUID();
        MenuRequest request = new MenuRequest(
                "김치찌개", 9000, MenuStatus.FORSALE,
                "매운 김치찌개", true, "프롬프트"
        );

        var result = new MenuResult(1L, "김치찌개", 9000, "AI설명", "FORSALE", true);
        Mockito.when(menuService.createMenu(eq(storeId), any(MenuCommand.class))).thenReturn(result);

        mockMvc.perform(post("/v1/menus")
                        .param("storeId", storeId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.menuName").value("김치찌개"));
    }


    @Test
    @DisplayName("PUT /v1/menus/{menuId} - 메뉴 수정 성공")
    void updateMenu_success() throws Exception {
        Long menuId = 10L;
        MenuRequest request = new MenuRequest(
                "된장찌개", 8000, MenuStatus.FORSALE,
                "구수한 된장찌개", false, null
        );

        var result = new MenuResult(menuId, "된장찌개", 8000, "구수한 된장찌개", "FORSALE", false);
        Mockito.when(menuService.updateMenu(eq(menuId), any(MenuCommand.class))).thenReturn(result);

        mockMvc.perform(put("/v1/menus/{menuId}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.menuName").value("된장찌개"))
                .andExpect(jsonPath("$.data.aiGenerated").value(false));
    }

    @Test
    @DisplayName("DELETE /v1/menus/{menuId} - 메뉴 삭제 성공")
    void deleteMenu_success() throws Exception {
        Long menuId = 10L;
        Mockito.doNothing().when(menuService).deleteMenu(menuId);

        mockMvc.perform(delete("/v1/menus/{menuId}", menuId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    @DisplayName("POST /v1/menus/status - 메뉴 상태 변경")
    void changeMenuStatus_success() throws Exception {
        var items = List.of(
                new MenuStatusRequest.MenuStatusChangeItem(1L, MenuStatus.HIDING),
                new MenuStatusRequest.MenuStatusChangeItem(2L, MenuStatus.FORSALE)
        );
        MenuStatusRequest request = new MenuStatusRequest(items);
        Mockito.when(menuService.changeMenuStatus(anyList(), anyString())).thenReturn(List.of(1L, 2L));

        mockMvc.perform(post("/v1/menus/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.updatedMenuIds[0]").value(1))
                .andExpect(jsonPath("$.data.updatedMenuIds[1]").value(2));
    }
}
