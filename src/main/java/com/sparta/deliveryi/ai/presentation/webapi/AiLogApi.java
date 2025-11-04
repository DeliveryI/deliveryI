package com.sparta.deliveryi.ai.presentation.webapi;

import com.sparta.deliveryi.ai.application.service.AiLogService;
import com.sparta.deliveryi.ai.domain.exception.AiCallFailedException;
import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ai/logs")
@RequiredArgsConstructor
public class AiLogApi {

    private final AiLogService aiLogService;

    // AI 설명 미리보기 API (DB 저장 없이 결과만 반환)
    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<String>> generateAiPreview(@RequestBody AiPreviewRequest request) {
        try {
            String fullPrompt = aiLogService.buildFullPrompt(request.prompt(), request.menuName());
            String result = aiLogService.callGeminiApiOnly(fullPrompt);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.successWithDataOnly(result));

        } catch (AiCallFailedException e) {
            return ResponseEntity
                    .status(e.getMessageCode().getStatus())
                    .body(ApiResponse.failure(e.getMessageCode().getCode()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("GLOBAL.UNKNOWN"));
        }
    }
    public record AiPreviewRequest(String prompt, String menuName) {}
}
