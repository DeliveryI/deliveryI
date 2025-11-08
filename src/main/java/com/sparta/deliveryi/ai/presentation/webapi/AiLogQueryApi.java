package com.sparta.deliveryi.ai.presentation.webapi;

import com.sparta.deliveryi.ai.application.service.AiLogQueryService;
import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.presentation.dto.AiLogQueryResponse;
import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI Query API", description = "AI 생성 로그 목록 조회 기능 제공")
@RestController
@RequestMapping("/v1/ai/logs")
@RequiredArgsConstructor
public class AiLogQueryApi {

    private final AiLogQueryService aiLogQueryService;

    @Operation(
            summary = "AI 로그 목록 조회",
            description = """
                    특정 메뉴(menuId)에 대한 AI 로그를 페이지 단위로 조회합니다.
                    - 최근 생성된 로그부터 정렬됩니다.
                    - AI 생성 결과, 프롬프트, 상태 등 AI 로그 정보를 반환합니다.
                    """
    )
    @GetMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('MANAGER','MASTER')")
    public ApiResponse<Page<AiLogQueryResponse>> getAiLogsByMenu(
            @Parameter(name = "menuId", description = "메뉴 ID", example = "1")
            @PathVariable Long menuId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<AiLog> aiLogs = aiLogQueryService.getAiLogsByMenu(menuId, pageable);
        Page<AiLogQueryResponse> response = aiLogs.map(AiLogQueryResponse::from);
        return ApiResponse.successWithDataOnly(response);
    }
}
