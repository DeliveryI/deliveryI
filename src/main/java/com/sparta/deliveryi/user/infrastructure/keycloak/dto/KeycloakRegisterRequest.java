package com.sparta.deliveryi.user.infrastructure.keycloak.dto;

import com.sparta.deliveryi.user.presentation.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record KeycloakRegisterRequest(
    @NotBlank @Size(min=4, max=10) @Pattern(regexp = "^[a-zA-Z0-9]+$")
    String username,

    @NotBlank @Size(min=8, max=15) @ValidPassword
    String password
) {}
