package com.sparta.deliveryi.ai.application.service;

import com.sparta.deliveryi.ai.domain.service.AiLogFinder;
import com.sparta.deliveryi.ai.presentation.dto.AiLogQueryResponse;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import com.sparta.deliveryi.menu.domain.exception.MenuNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiLogQueryService {

    private final AiLogFinder aiLogFinder;
    private final MenuRepository menuRepository;

    private static final List<Integer> ALLOWED_PAGE_SIZES = List.of(10, 30, 50);
    private static final int DEFAULT_PAGE_SIZE = 10;

    public Page<AiLogQueryResponse> getAiLogsByMenu(Long menuId, Pageable pageable) {

        if (!menuRepository.existsById(menuId)) {
            throw new MenuNotFoundException();
        }

        Pageable validatedPageable = adjustPageSize(pageable);

        return aiLogFinder.findAllByMenuId(menuId, validatedPageable);
    }

    // 허용된 페이지 크기(10, 30, 50) 외의 값은 기본 10으로 고정
    private Pageable adjustPageSize(Pageable pageable) {
        int requestedSize = pageable.getPageSize();
        int validatedSize = ALLOWED_PAGE_SIZES.contains(requestedSize) ? requestedSize : DEFAULT_PAGE_SIZE;
        return PageRequest.of(pageable.getPageNumber(), validatedSize, pageable.getSort());
    }
}
