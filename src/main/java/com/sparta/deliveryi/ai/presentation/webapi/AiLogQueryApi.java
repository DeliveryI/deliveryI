package com.sparta.deliveryi.ai.presentation.webapi;

import com.sparta.deliveryi.ai.application.service.AiLogQueryService;
import com.sparta.deliveryi.ai.presentation.AiLogQueryResponse;
import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ai/logs")
@RequiredArgsConstructor
public class AiLogQueryApi {

    private final AiLogQueryService aiLogQueryService;

    @GetMapping("/{menuId}")
    public ApiResponse<Page<AiLogQueryResponse>> getAiLogsByMenu(
            @PathVariable Long menuId,
            @PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ApiResponse.successWithDataOnly(aiLogQueryService.getAiLogsByMenu(menuId, pageable));
    }
}
