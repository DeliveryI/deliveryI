package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.ai.application.service.AiApplicationService;
import com.sparta.deliveryi.menu.application.dto.*;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.exception.*;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import com.sparta.deliveryi.menu.domain.service.MenuDescriptionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public MenuResult createMenu(UUID storeId, MenuCommand command) {
        String username = getCurrentUsername();

        Menu menu = Menu.create(
                storeId,
                command.menuName(),
                command.menuPrice(),
                command.menuDescription(),
                command.menuStatus(),
                username
        );
        menuRepository.save(menu);

        if (command.aiGenerate()) {
            String fullPrompt = descriptionGenerator.buildFullPrompt(command.prompt(), command.menuName());
            var result = aiApplicationService.generate(
                    menu.getMenuId(),
                    command.menuName(),
                    fullPrompt,
                    true,
                    username
            );

            menu.update(
                    command.menuName(),
                    command.menuPrice(),
                    result.description(),
                    command.menuStatus(),
                    username
            );
        }

        return MenuResult.from(menu, command.aiGenerate());
    }

    public MenuResult updateMenu(Long menuId, MenuCommand command) {
        String username = getCurrentUsername();
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (menu.isDeleted()) {
            throw new MenuDeletedException();
        }

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
                    username
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
                username
        );

        return MenuResult.from(menu, aiGenerated);
    }

    public void deleteMenu(Long menuId) {
        String username = getCurrentUsername();

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (menu.isDeleted()) {
            throw new MenuDeletedException();
        }

        menu.markDeleted(username);
    }

    public List<Long> changeMenuStatus(List<MenuStatusChangeCommand> commands, String updatedBy) {
        return commands.stream().map(cmd -> {
            Menu menu = menuRepository.findById(cmd.menuId())
                    .orElseThrow(MenuNotFoundException::new);
            menu.changeStatus(cmd.status(), updatedBy);
            return menu.getMenuId();
        }).toList();
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName()
                : "anonymousUser";
    }
}
