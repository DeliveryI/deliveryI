package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.ai.application.service.AiApplicationService;
import com.sparta.deliveryi.menu.application.dto.*;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.exception.*;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import com.sparta.deliveryi.menu.domain.service.MenuDescriptionGenerator;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final AiApplicationService aiApplicationService;
    private final MenuDescriptionGenerator descriptionGenerator;
    private final StoreFinder storeFinder;
    private final UserRolePolicy userRolePolicy;

    public MenuResult createMenu(UUID storeId, MenuCommand command, UUID requestId) {
        checkStoreAccess(storeId, requestId);

        Menu menu = Menu.create(
                storeId,
                command.menuName(),
                command.menuPrice(),
                command.menuDescription(),
                command.menuStatus(),
                requestId.toString()
        );
        menuRepository.save(menu);

        if (command.aiGenerate()) {
            String fullPrompt = descriptionGenerator.buildFullPrompt(command.prompt(), command.menuName());
            var result = aiApplicationService.generate(
                    menu.getMenuId(),
                    command.menuName(),
                    fullPrompt,
                    true,
                    requestId.toString()
            );

            menu.update(
                    command.menuName(),
                    command.menuPrice(),
                    result.description(),
                    command.menuStatus(),
                    requestId.toString()
            );
        }

        return MenuResult.from(menu, command.aiGenerate());
    }


    public MenuResult updateMenu(Long menuId, MenuCommand command, UUID storeId, UUID requestId) {
        checkStoreAccess(storeId, requestId);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (menu.isDeleted()) throw new MenuDeletedException();

        String description;
        boolean aiGenerated;

        if (!command.aiGenerate()) {
            description = command.menuDescription();
            aiGenerated = false;
        } else {
            String fullPrompt = descriptionGenerator.buildFullPrompt(command.prompt(), command.menuName());
            var result = aiApplicationService.generate(
                    menuId,
                    command.menuName(),
                    fullPrompt,
                    true,
                    requestId.toString()
            );

            description = (result.description() != null && !result.description().isBlank())
                    ? result.description()
                    : "AI 설명 생성 실패";
            aiGenerated = result.aiGenerated();
        }

        menu.update(
                command.menuName(),
                command.menuPrice(),
                description,
                command.menuStatus(),
                requestId.toString()
        );

        return MenuResult.from(menu, aiGenerated);
    }

    public void deleteMenu(Long menuId, UUID storeId, UUID requestId) {
        checkStoreAccess(storeId, requestId);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (menu.isDeleted()) throw new MenuDeletedException();

        menu.delete();
    }

    public List<Long> changeMenuStatus(List<MenuStatusChangeCommand> commands, UUID storeId, UUID requestId) {
        checkStoreAccess(storeId, requestId);

        return commands.stream().map(cmd -> {
            Menu menu = menuRepository.findById(cmd.menuId())
                    .orElseThrow(MenuNotFoundException::new);
            menu.changeStatus(cmd.status(), requestId.toString());
            return menu.getMenuId();
        }).toList();
    }

    // OWNER는 자신의 가게만, MANAGER/MASTER는 전체 접근 가능
    private void checkStoreAccess(UUID storeId, UUID requestId) {
        // 관리자는 모든 가게 접근 가능
        if (userRolePolicy.isAdmin(requestId)) return;

        // OWNER는 자신의 가게만 접근 가능
        if (userRolePolicy.isOwner(requestId)) {
            var store = storeFinder.find(StoreId.of(storeId));
            if (!store.getOwner().getId().equals(requestId)) {
                throw new MenuStoreAccessDeniedException();
            }
        }
    }
}
