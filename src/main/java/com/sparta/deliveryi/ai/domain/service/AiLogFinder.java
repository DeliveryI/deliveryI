package com.sparta.deliveryi.ai.domain.service;

import com.sparta.deliveryi.ai.presentation.AiLogQueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AiLogFinder {
    Page<AiLogQueryResponse> findAllByMenuId(Long menuId, Pageable pageable);
}