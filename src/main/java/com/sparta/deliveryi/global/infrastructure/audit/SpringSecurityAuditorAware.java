package com.sparta.deliveryi.global.infrastructure.audit;

import io.micrometer.common.lang.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Spring Data JPA의 감사(audit) 기능을 위한 AuditorAware 구현체.
 *
 * <p>
 * 현재 인증된 회원의 계정(username)을 생성 및 수정자(auditor) 정보로 제공합니다.
 * 인증 정보가 없거나 인증되지 않은 경우 빈 Optional을 반환합니다.
 * </p>
 *
 * @author 김형섭
 * @since 2025.10.30
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    /**
     * 현재 보안 컨텍스트(SecurityContext)에 저장된 인증(Authentication) 객체로부터
     * 현재 회원의 계정을 가져와 감사자(auditor)로 반환합니다.
     *
     * @return 인증된 회원의 계정을 담은 Optional, 인증 정보가 없거나 인증되지 않은 경우 빈 Optional
     */
    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        return Optional.ofNullable(authentication.getName());
    }
}
