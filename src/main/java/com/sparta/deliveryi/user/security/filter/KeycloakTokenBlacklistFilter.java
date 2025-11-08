package com.sparta.deliveryi.user.security.filter;

import com.sparta.deliveryi.global.exception.GlobalMessageCode;
import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.user.security.TokenBlacklist;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class KeycloakTokenBlacklistFilter extends OncePerRequestFilter {

    private final TokenBlacklist tokenBlacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Jwt jwt = jwtAuthenticationToken.getToken();
            String token = jwt.getTokenValue();

            if (tokenBlacklist.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");

                // ApiResponse 생성
                ApiResponse<Void> apiResponse = ApiResponse.failure(GlobalMessageCode.ACCESS_TOKEN_EXPIRED.getCode());

                // ObjectMapper로 JSON 직렬화
                String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(apiResponse);
                response.getWriter().write(json);

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
