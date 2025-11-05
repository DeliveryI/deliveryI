package com.sparta.deliveryi.ai.infrastructure.service;

import com.sparta.deliveryi.ai.domain.AiLog;
import com.sparta.deliveryi.ai.domain.AiStatus;
import com.sparta.deliveryi.global.infrastructure.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QueryDslConfig.class, AiLogFinderImpl.class})
@DisplayName("AiLogFinderImpl 통합 테스트")
class AiLogFinderImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private AiLogFinderImpl aiLogFinder;

    @Test
    @DisplayName("menuId 기준으로 AI 로그 목록 조회 성공")
    void findAllByMenuId_success() {
        // given
        em.persist(AiLog.create(10L, "프롬프트1", "전체1", "응답1", AiStatus.SUCCESS, "tester"));
        em.persist(AiLog.create(10L, "프롬프트2", "전체2", "응답2", AiStatus.SUCCESS, "tester"));
        em.flush();

        // when
        Page<AiLog> result = aiLogFinder.findAllByMenuId(10L, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getMenuId()).isEqualTo(10L);
    }
}
