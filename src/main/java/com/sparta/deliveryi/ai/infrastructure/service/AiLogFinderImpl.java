package com.sparta.deliveryi.ai.infrastructure.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.QAiLog;
import com.sparta.deliveryi.ai.domain.service.AiLogFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AiLogFinderImpl implements AiLogFinder {

    private final JPAQueryFactory queryFactory;
    private final QAiLog aiLog = QAiLog.aiLog;

    @Override
    public Page<AiLog> findAllByMenuId(Long menuId, Pageable pageable) {
        List<AiLog> contents = queryFactory
                .selectFrom(aiLog)
                .where(aiLog.menuId.eq(menuId))
                .orderBy(aiLog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Objects.requireNonNullElse(
                queryFactory
                        .select(aiLog.count())
                        .from(aiLog)
                        .where(aiLog.menuId.eq(menuId))
                        .fetchOne(),
                0L
        );

        return new PageImpl<>(contents, pageable, total);

    }
}
