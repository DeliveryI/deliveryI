package com.sparta.deliveryi.ai.application.service;

import com.sparta.deliveryi.ai.domain.exception.AiCallFailedException;
import com.sparta.deliveryi.ai.infrastructure.GeminiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AiLogService {

    private final GeminiClient geminiClient;

    public String callGeminiApiOnly(String fullPrompt) throws AiCallFailedException {
        return geminiClient.requestDescription(fullPrompt);
    }
}
