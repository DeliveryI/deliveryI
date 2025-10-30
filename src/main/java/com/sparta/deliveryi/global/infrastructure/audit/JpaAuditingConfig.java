package com.sparta.deliveryi.global.infrastructure.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@Configuration
public class JpaAuditingConfig {
}