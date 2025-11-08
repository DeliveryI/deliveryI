package com.sparta.deliveryi.user.security.config;

import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakClientRoleConverter;
import com.sparta.deliveryi.user.security.TokenBlacklist;
import com.sparta.deliveryi.user.security.filter.KeycloakTokenBlacklistFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenBlacklist tokenBlacklist;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
        conv.setJwtGrantedAuthoritiesConverter(new KeycloakClientRoleConverter());

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .oauth2Login(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(c -> c
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(conv))
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        http.addFilterAfter(new KeycloakTokenBlacklistFilter(tokenBlacklist), BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
