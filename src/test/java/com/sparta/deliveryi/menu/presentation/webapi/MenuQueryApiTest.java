package com.sparta.deliveryi.menu.presentation.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.menu.application.service.MenuQueryService;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuQueryApi.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MenuQueryApiTest.TestConfig.class)
@DisplayName("MenuQueryApi 테스트")
class MenuQueryApiTest {

    @Autowired MockMvc mockMvc;
    @Autowired MenuQueryService menuQueryService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        MenuQueryService menuQueryService() {
            return Mockito.mock(MenuQueryService.class);
        }
    }

    @Test
    @DisplayName("GET /v1/stores/{storeId}/menus - 메뉴 목록 조회")
    void getMenusByStore_success() throws Exception {
        Menu menu1 = Menu.create(UUID.randomUUID(), "비빔밥", 9000, "한식 대표 메뉴", MenuStatus.FORSALE, "tester");
        Menu menu2 = Menu.create(UUID.randomUUID(), "불고기", 12000, "고기 요리", MenuStatus.FORSALE, "tester");
        List<Menu> list = List.of(menu1, menu2);
        Page<Menu> page = new PageImpl<>(list, PageRequest.of(0, 10), list.size());

        Mockito.when(menuQueryService.getMenusByStore(
                anyString(), any(), anyString(), any(), anyInt(), anyInt(), anyString(), anyString()
        )).thenReturn(page);

        mockMvc.perform(get("/v1/stores/{storeId}/menus", "1234")
                        .param("role", "CUSTOMER")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .param("direction", "DESC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].menuName").value("비빔밥"))
                .andExpect(jsonPath("$.data.content[1].menuName").value("불고기"));
    }

    @Test
    @DisplayName("GET /v1/stores/menus/{menuId} - 메뉴 상세 조회")
    void getMenu_success() throws Exception {
        Menu menu = Menu.create(UUID.randomUUID(), "냉면", 8000, "여름별미", MenuStatus.FORSALE, "tester");
        Mockito.when(menuQueryService.getMenu(anyLong())).thenReturn(menu);

        mockMvc.perform(get("/v1/stores/menus/{menuId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.menuName").value("냉면"))
                .andExpect(jsonPath("$.data.menuPrice").value(8000));
    }
}
