package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.ai.application.service.AiApplicationService;
import com.sparta.deliveryi.ai.application.service.AiApplicationService.AiResult;
import com.sparta.deliveryi.menu.application.dto.MenuCommand;
import com.sparta.deliveryi.menu.application.dto.MenuResult;
import com.sparta.deliveryi.menu.application.dto.MenuStatusChangeCommand;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import com.sparta.deliveryi.menu.domain.service.MenuDescriptionGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuService 단위 테스트")
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private AiApplicationService aiApplicationService;

    @Mock
    private MenuDescriptionGenerator descriptionGenerator;

    @Test
    @DisplayName("AI 사용 시 메뉴 생성 성공")
    void createMenu_withAi_success() {
        UUID storeId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        MenuCommand command = new MenuCommand(
                "김치찌개",
                8000,
                MenuStatus.FORSALE,
                "AI로 설명 생성",
                true,
                "매운 김치찌개 설명"
        );

        given(descriptionGenerator.buildFullPrompt(anyString(), anyString()))
                .willReturn("full prompt");
        given(aiApplicationService.generate(any(), anyString(), anyString(), anyBoolean(), anyString()))
                .willReturn(new AiResult("AI 설명", true, "fullPrompt"));

        MenuResult result = menuService.createMenu(storeId, command, requestId);

        assertThat(result.menuName()).isEqualTo("김치찌개");
        assertThat(result.aiGenerated()).isTrue();
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    @DisplayName("AI 미사용 시 메뉴 생성 성공")
    void createMenu_withoutAi_success() {
        UUID storeId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        MenuCommand command = new MenuCommand(
                "된장찌개",
                7000,
                MenuStatus.FORSALE,
                "직접 입력된 설명",
                false,
                null
        );

        MenuResult result = menuService.createMenu(storeId, command, requestId);

        assertThat(result.menuName()).isEqualTo("된장찌개");
        assertThat(result.aiGenerated()).isFalse();
        verify(menuRepository).save(any(Menu.class));
        verify(aiApplicationService, never()).generate(any(), anyString(), anyString(), anyBoolean(), anyString());
    }

    @Test
    @DisplayName("메뉴 수정 성공 - AI 호출 포함")
    void updateMenu_withAi_success() {
        UUID storeId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        Menu menu = Menu.create(storeId, "불고기", 10000, "기존 설명", MenuStatus.FORSALE, "tester");
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
        given(descriptionGenerator.buildFullPrompt(anyString(), anyString()))
                .willReturn("full prompt");
        given(aiApplicationService.generate(any(), anyString(), anyString(), anyBoolean(), anyString()))
                .willReturn(new AiResult("AI 새 설명", true, "prompt"));

        MenuCommand command = new MenuCommand("불고기정식", 12000, MenuStatus.FORSALE, "수정 설명", true, "새 프롬프트");

        MenuResult result = menuService.updateMenu(1L, command, storeId, requestId);

        assertThat(result.menuName()).isEqualTo("불고기정식");
        assertThat(result.aiGenerated()).isTrue();
        verify(aiApplicationService).generate(any(), anyString(), anyString(), anyBoolean(), anyString());
    }

    @Test
    @DisplayName("메뉴 삭제 성공")
    void deleteMenu_success() {
        UUID storeId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        Menu menu = Menu.create(storeId, "라면", 6000, "간편식", MenuStatus.FORSALE, "tester");
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

        menuService.deleteMenu(1L, storeId, requestId);

        assertThat(menu.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("여러 메뉴 상태 변경 성공")
    void changeMenuStatus_success() throws Exception {
        UUID storeId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        Menu m1 = Menu.create(storeId, "라면", 5000, "간편식", MenuStatus.FORSALE, "tester");
        Menu m2 = Menu.create(storeId, "냉면", 9000, "여름별미", MenuStatus.HIDING, "tester");

        Field idField = Menu.class.getDeclaredField("menuId");
        idField.setAccessible(true);
        idField.set(m1, 1L);
        idField.set(m2, 2L);

        given(menuRepository.findById(1L)).willReturn(Optional.of(m1));
        given(menuRepository.findById(2L)).willReturn(Optional.of(m2));

        List<MenuStatusChangeCommand> cmds = List.of(
                new MenuStatusChangeCommand(1L, MenuStatus.HIDING),
                new MenuStatusChangeCommand(2L, MenuStatus.FORSALE)
        );

        List<Long> changed = menuService.changeMenuStatus(cmds, storeId, requestId);

        assertThat(changed).hasSize(2);
        assertThat(m1.getMenuStatus()).isEqualTo(MenuStatus.HIDING);
        assertThat(m2.getMenuStatus()).isEqualTo(MenuStatus.FORSALE);
    }
}
