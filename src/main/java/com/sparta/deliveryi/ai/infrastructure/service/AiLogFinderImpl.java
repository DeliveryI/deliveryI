package com.sparta.deliveryi.ai.infrastructure.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.ai.domain.QAiLog;
import com.sparta.deliveryi.ai.domain.service.AiLogFinder;
import com.sparta.deliveryi.ai.presentation.dto.AiLogQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AiLogFinderImpl implements AiLogFinder {

    private final JPAQueryFactory queryFactory;
    private final QAiLog aiLog = QAiLog.aiLog;

    @Override
    public Page<AiLogQueryResponse> findAllByMenuId(Long menuId, Pageable pageable) {
        List<AiLogQueryResponse> contents = queryFactory
                .select(
                        com.querydsl.core.types.Projections.constructor(
                                AiLogQueryResponse.class,
                                aiLog.aiId,
                                aiLog.menuId,
                                aiLog.prompt,
                                aiLog.fullPrompt,
                                aiLog.response,
                                aiLog.aiStatus,
                                aiLog.createdBy,
                                aiLog.createdAt.stringValue()
                        )
                )
                .from(aiLog)
                .where(aiLog.menuId.eq(menuId))
                .orderBy(aiLog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long total = queryFactory
                .select(aiLog.count())
                .from(aiLog)
                .where(aiLog.menuId.eq(menuId))
                .fetchOne();


        return new PageImpl<>(contents, pageable, total == null ? 0 : total);
    }
}
