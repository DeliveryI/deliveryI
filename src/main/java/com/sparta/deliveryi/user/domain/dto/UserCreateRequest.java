package com.sparta.deliveryi.user.domain.dto;

import com.sparta.deliveryi.user.infrastructure.keycloak.dto.KeycloakUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserCreateRequest(
        @Valid KeycloakUser keycloakUser,
        @NotBlank @Size(min=1, max=20) String nickname,
        @NotBlank String userPhone,
        String currentAddress
) {}
