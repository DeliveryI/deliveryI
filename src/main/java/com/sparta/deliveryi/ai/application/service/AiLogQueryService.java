package com.sparta.deliveryi.ai.application.service;

import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.exception.AiMenuNotFoundException;
import com.sparta.deliveryi.ai.domain.service.AiLogFinder;
import com.sparta.deliveryi.ai.domain.service.MenuLookupClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiLogQueryService {

    private final AiLogFinder aiLogFinder;
    private final MenuLookupClient menuLookupClient;

    private static final List<Integer> ALLOWED_PAGE_SIZES = List.of(10, 30, 50);
    private static final int DEFAULT_PAGE_SIZE = 10;

    public Page<AiLog> getAiLogsByMenu(Long menuId, Pageable pageable) {
        if (!menuLookupClient.existsMenuById(menuId)) {
            throw new AiMenuNotFoundException();
        }

        Pageable validatedPageable = adjustPageSize(pageable);

        return aiLogFinder.findAllByMenuId(menuId, validatedPageable); // ✅ domain 반환
    }

    private Pageable adjustPageSize(Pageable pageable) {
        int requestedSize = pageable.getPageSize();
        int validatedSize = ALLOWED_PAGE_SIZES.contains(requestedSize) ? requestedSize : DEFAULT_PAGE_SIZE;
        return PageRequest.of(pageable.getPageNumber(), validatedSize, pageable.getSort());
    }
}
