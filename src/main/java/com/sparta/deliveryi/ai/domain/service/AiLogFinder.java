package com.sparta.deliveryi.ai.domain.service;

import com.sparta.deliveryi.ai.domain.AiLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AiLogFinder {
    Page<AiLog> findAllByMenuId(Long menuId, Pageable pageable);
}
