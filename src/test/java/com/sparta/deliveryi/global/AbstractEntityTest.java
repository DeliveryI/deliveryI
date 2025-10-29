package com.sparta.deliveryi.global;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractEntityTest {
    private static final String USERNAME = "username";

    private AbstractEntity entity;

    @BeforeEach
    void setUp() {
        entity = new AbstractEntity() {};
    }

    @Test
    void testCreateBy() {
        entity.createBy(USERNAME);

        assertThat(entity.getCreatedBy()).isEqualTo(USERNAME);
    }

    @Test
    void testUpdateBy() {
        entity.updateBy(USERNAME);

        assertThat(entity.getUpdatedBy()).isEqualTo(USERNAME);
    }

    @Test
    void testDeleteBy() {
        entity.deleteBy(USERNAME);

        assertThat(entity.getDeletedBy()).isEqualTo(USERNAME);
    }

    @Test
    void testDelete() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            Authentication authentication = Mockito.mock(Authentication.class);
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);

            Mockito.when(authentication.getName()).thenReturn(USERNAME);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            entity.delete();

            assertThat(entity.getDeletedAt()).isNotNull();
            assertThat(entity.getDeletedAt()).isBeforeOrEqualTo(LocalDateTime.now());

            assertThat(entity.getDeletedBy()).isEqualTo(USERNAME);
        }
    }

    @Test
    void testDeleteWithoutAuthentication() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(null);

            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            entity.delete();

            assertThat(entity.getDeletedBy()).isEqualTo("anonymousUser");
        }
    }

    @Test
    void testEqual() {
        entity.createBy(USERNAME);
        entity.updateBy(USERNAME);

        AbstractEntity newEntity = new AbstractEntity() {};
        assertThat(newEntity.equals(entity)).isFalse();

        newEntity.createBy(USERNAME);
        newEntity.updateBy(USERNAME);
        assertThat(newEntity.equals(entity)).isTrue();
    }
}