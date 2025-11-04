package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.AiStatus;
import com.sparta.deliveryi.ai.domain.service.AiLogRegister;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.exception.MenuDeletedException;
import com.sparta.deliveryi.menu.domain.exception.MenuNotFoundException;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import com.sparta.deliveryi.menu.domain.service.MenuDescriptionGenerator;
import com.sparta.deliveryi.menu.presentation.dto.MenuRemoveResponse;
import com.sparta.deliveryi.menu.presentation.dto.MenuRequest;
import com.sparta.deliveryi.menu.presentation.dto.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final AiLogRegister aiLogRegister;
    private final MenuDescriptionGenerator descriptionGenerator;

    // 메뉴 생성
    public MenuResponse createMenu(UUID storeId, MenuRequest request) {
        String username = getCurrentUsername();

        var result = descriptionGenerator.generate(
                request.prompt(),
                request.menuName(),
                request.menuDescription(),
                request.aiGenerate()
        );

        Menu menu = Menu.create(
                storeId,
                request.menuName(),
                request.menuPrice(),
                result.description(),
                request.menuStatus(),
                username
        );
        menuRepository.save(menu);

        saveAiLogIfNeeded(menu, request, result, username);
        return MenuResponse.from(menu, result.aiGenerated());
    }

    // 메뉴 수정
    public MenuResponse updateMenu(Long menuId, MenuRequest request) {
        String username = getCurrentUsername();

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (menu.isDeleted()) {
            throw new MenuDeletedException();
        }

        var result = descriptionGenerator.generate(
                request.prompt(),
                request.menuName(),
                request.menuDescription(),
                request.aiGenerate()
        );

        menu.update(
                request.menuName(),
                request.menuPrice(),
                result.description(),
                request.menuStatus(),
                username
        );

        saveAiLogIfNeeded(menu, request, result, username);
        return MenuResponse.from(menu, result.aiGenerated());
    }


    // 메뉴 삭제
    @Transactional
    public MenuRemoveResponse deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if (menu.isDeleted()) {
            throw new MenuDeletedException();
        }

        menu.delete();
        return MenuRemoveResponse.from(menu);
    }

    // Ai 로그 저장
    private void saveAiLogIfNeeded(Menu menu, MenuRequest request, MenuDescriptionGenerator.Result result, String username) {
        if (result.aiGenerated() && result.fullPrompt() != null) {
            AiLog aiLog = AiLog.create(
                    menu.getMenuId(),
                    request.menuName(),
                    result.fullPrompt(),
                    result.description(),
                    AiStatus.SUCCESS,
                    username
            );
            aiLogRegister.save(aiLog);
        }
    }

    // 로그인 사용자 가져오기
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName()
                : "anonymousUser";
    }
}
