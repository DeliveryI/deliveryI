package com.sparta.deliveryi;

import com.sparta.deliveryi.user.application.service.UserRoleService;
import com.sparta.deliveryi.user.domain.UserRole;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@TestConfiguration
public class DeliveryITestConfiguration {
    @Bean
    public UserRoleService userRoleService() {
        return new UserRoleService() {
            @Override
            public boolean isAdmin(UUID userId) {
                return "00000000-0000-0000-0000-000000000000"
                        .equals(userId.toString());
            }

            @Override
            public void updateUserRole(UUID userId, UserRole role) {

            }
        };
    }
}
