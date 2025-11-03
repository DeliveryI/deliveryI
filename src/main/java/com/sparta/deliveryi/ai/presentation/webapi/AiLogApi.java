package com.sparta.deliveryi.ai.presentation.webapi;

import com.sparta.deliveryi.ai.application.service.AiLogService;
import com.sparta.deliveryi.ai.presentation.dto.AiLogRequest;
import com.sparta.deliveryi.ai.presentation.dto.AiLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ai/logs")
@RequiredArgsConstructor
public class AiLogApi {

    private final AiLogService aiLogService;

    @PostMapping
    public ResponseEntity<AiLogResponse> createAiLog(
            @RequestBody AiLogRequest requestDto
    ) {
        AiLogResponse response = aiLogService.createAiLog(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}