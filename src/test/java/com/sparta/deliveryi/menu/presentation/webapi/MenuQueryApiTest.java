package com.sparta.deliveryi.menu.presentation.webapi;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import com.sparta.deliveryi.menu.application.service.MenuQueryService;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuQueryApi.class)
@AutoConfigureMockMvc
@Import(MenuQueryApiTest.TestConfig.class)
@DisplayName("MenuQueryApi 테스트")
class MenuQueryApiTest {

    @Autowired MockMvc mockMvc;
    @Autowired MenuQueryService menuQueryService;

    @BeforeEach
    void setUp() throws Exception {
        TestMessageResolverInitializer.initializeFromResourceBundle();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        MenuQueryService menuQueryService() {
            return Mockito.mock(MenuQueryService.class);
        }
    }

    private Jwt createTestJwt(String role) {
        Map<String, Object> realmAccess = Map.of("roles", List.of(role));
        Map<String, Object> claims = Map.of(
                "sub", UUID.randomUUID().toString(),
                "preferred_username", "tester",
                "realm_access", realmAccess
        );

        return new Jwt(
                "fake-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                claims
        );
    }

    @Test
    @DisplayName("GET /v1/stores/{storeId}/menus - 메뉴 목록 조회")
    void getMenusByStore_success() throws Exception {
        Menu menu1 = Menu.create(UUID.randomUUID(), "비빔밥", 9000, "한식 대표 메뉴", MenuStatus.FORSALE, "tester");
        Menu menu2 = Menu.create(UUID.randomUUID(), "불고기", 12000, "고기 요리", MenuStatus.FORSALE, "tester");
        List<Menu> list = List.of(menu1, menu2);
        Page<Menu> page = new PageImpl<>(list, PageRequest.of(0, 10), list.size());

        Mockito.when(menuQueryService.getMenusByStore(
                any(UUID.class),
                any(UUID.class),
                anyString(),
                any(),
                anyInt(),
                anyInt(),
                anyString(),
                anyString()
        )).thenReturn(page);

        mockMvc.perform(get("/v1/stores/{storeId}/menus", UUID.randomUUID())
                        .with(jwt().jwt(createTestJwt("CUSTOMER")))
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
    @DisplayName("GET /v1/stores/{storeId}/menus/{menuId} - 메뉴 상세 조회 (CUSTOMER 권한, 감사정보 미포함)")
    void getMenu_customer_noAuditFields() throws Exception {
        UUID storeId = UUID.randomUUID();
        Menu menu = Menu.create(storeId, "냉면", 8000, "여름별미", MenuStatus.FORSALE, "tester");

        Mockito.when(menuQueryService.getMenu(anyLong(), any(UUID.class), any(UUID.class), anyString()))
                .thenReturn(menu);

        mockMvc.perform(get("/v1/stores/{storeId}/menus/{menuId}", storeId, 1L)
                        .with(jwt().jwt(createTestJwt("CUSTOMER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.menuName").value("냉면"))
                .andExpect(jsonPath("$.data.menuPrice").value(8000))
                .andExpect(jsonPath("$.data.createdBy").doesNotExist())
                .andExpect(jsonPath("$.data.updatedAt").doesNotExist())
                .andExpect(jsonPath("$.data.deletedBy").doesNotExist());
    }

    @Test
    @DisplayName("GET /v1/stores/{storeId}/menus/{menuId} - 메뉴 상세 조회 (MANAGER 권한, 감사정보 포함)")
    void getMenu_manager_withAuditFields() throws Exception {
        UUID storeId = UUID.randomUUID();
        Menu menu = Menu.create(storeId, "된장찌개", 7000, "구수한 한식 메뉴", MenuStatus.FORSALE, "managerUser");

        Mockito.when(menuQueryService.getMenu(anyLong(), any(UUID.class), any(UUID.class), anyString()))
                .thenReturn(menu);

        mockMvc.perform(get("/v1/stores/{storeId}/menus/{menuId}", storeId, 1L)
                        .with(jwt().jwt(createTestJwt("MANAGER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.menuName").value("된장찌개"))
                .andExpect(jsonPath("$.data.menuPrice").value(7000))
                .andExpect(jsonPath("$.data.createdBy").value("managerUser"))
                .andExpect(jsonPath("$.data.createdAt").exists());
    }
}
