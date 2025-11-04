package com.sparta.deliveryi.ai.application.service;

import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.AiStatus;
import com.sparta.deliveryi.ai.domain.service.AiClient;
import com.sparta.deliveryi.ai.domain.service.AiLogRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AI 관련 애플리케이션 서비스.
 * - 외부 AI 호출
 * - AI 로그 저장
 * 이 두 가지를 ai 모듈 내부에서 담당.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AiApplicationService {

    private final AiClient aiClient;
    private final AiLogRegister aiLogRegister;

    public AiResult generate(Long menuId, String menuName, String fullPrompt, boolean aiGenerate, String username) {
        if (!aiGenerate) {
            return new AiResult(null, false, fullPrompt);
        }

        String response = aiClient.requestDescription(fullPrompt);

        AiLog aiLog = AiLog.create(
                menuId,
                menuName,
                fullPrompt,
                response,
                AiStatus.SUCCESS,
                username
        );
        aiLogRegister.save(aiLog);

        return new AiResult(response, true, fullPrompt);
    }

    public record AiResult(String description, boolean aiGenerated, String fullPrompt) {}
}
