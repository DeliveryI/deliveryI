package com.sparta.deliveryi;

import com.sparta.deliveryi.user.domain.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public class MockUtils {
    static final String MANAGER_UUID = "00000000-0000-0000-0000-000000000000";
    static final String OWNER_UUID = "11111111-1111-1111-1111-111111111111";
    static final String CUSTOMER_UUID = "22222222-2222-2222-2222-222222222222";

    public static String managerId() {
        return MANAGER_UUID;
    }

    public static String ownerId() {
        return OWNER_UUID;
    }

    public static String customerId() {
        return CUSTOMER_UUID;
    }

    public static void mockedMangerToken() {
        mockedToken(MANAGER_UUID, UserRole.MANAGER.name());
    }

    public static void mockedOwnerToken() {
        mockedToken(OWNER_UUID, UserRole.OWNER.name());
    }

    public static void mockedCustomerToken() {
        mockedToken(CUSTOMER_UUID, UserRole.CUSTOMER.name());
    }

    private static void mockedToken(String subject, String role) {
        Jwt jwt = Jwt.withTokenValue("dummy-token")
                .header("alg", "none")
                .subject(subject)
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(
                jwt,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}