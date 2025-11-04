package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.AiStatus;
import com.sparta.deliveryi.ai.domain.service.AiLogRegister;
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
    private final AiLogRegister aiLogRegister;
    private final MenuDescriptionGenerator descriptionGenerator;

    public MenuResult createMenu(UUID storeId, MenuCommand command) {
        String username = getCurrentUsername();

        var result = descriptionGenerator.generate(
                command.prompt(),
                command.menuName(),
                command.menuDescription(),
                command.aiGenerate()
        );

        Menu menu = Menu.create(
                storeId,
                command.menuName(),
                command.menuPrice(),
                result.description(),
                command.menuStatus(),
                username
        );
        menuRepository.save(menu);

        saveAiLogIfNeeded(menu, command, result, username);
        return MenuResult.from(menu, result.aiGenerated());
    }

    public MenuResult updateMenu(Long menuId, MenuCommand command) {
        String username = getCurrentUsername();
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (menu.isDeleted()) {
            throw new MenuDeletedException();
        }

        var result = descriptionGenerator.generate(
                command.prompt(),
                command.menuName(),
                command.menuDescription(),
                command.aiGenerate()
        );

        menu.update(
                command.menuName(),
                command.menuPrice(),
                result.description(),
                command.menuStatus(),
                username
        );

        saveAiLogIfNeeded(menu, command, result, username);
        return MenuResult.from(menu, result.aiGenerated());
    }

    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
        if (menu.isDeleted()) throw new MenuDeletedException();
        menu.delete();
    }

    public List<Long> changeMenuStatus(List<MenuStatusChangeCommand> commands, String updatedBy) {
        return commands.stream().map(cmd -> {
            Menu menu = menuRepository.findById(cmd.menuId())
                    .orElseThrow(MenuNotFoundException::new);
            menu.changeStatus(cmd.status(), updatedBy);
            return menu.getMenuId();
        }).toList();
    }

    private void saveAiLogIfNeeded(Menu menu, MenuCommand command, MenuDescriptionGenerator.Result result, String username) {
        if (result.aiGenerated() && result.fullPrompt() != null) {
            AiLog aiLog = AiLog.create(
                    menu.getMenuId(),
                    command.menuName(),
                    result.fullPrompt(),
                    result.description(),
                    AiStatus.SUCCESS,
                    username
            );
            aiLogRegister.save(aiLog);
        }
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName()
                : "anonymousUser";
    }
}
