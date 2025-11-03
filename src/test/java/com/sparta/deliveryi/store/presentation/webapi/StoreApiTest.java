package com.sparta.deliveryi.store.presentation.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.service.StoreRegisterService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

import static com.sparta.deliveryi.AssertThatUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class StoreApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final StoreRegisterService registerService;

    @Test
    @WithMockUser
    void register() throws JsonProcessingException {
        StoreRegisterRequest request = StoreFixture.createStoreRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue())
                .hasPathSatisfying("$.data", notNull());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void acceptRegisterRequest() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/accept", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void acceptRegisterRequestIfUnauthorized() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/accept", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void rejectRegisterRequest() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/reject", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void rejectRegisterRequestIfUnauthorized() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/reject", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @TestConfiguration
    @EnableMethodSecurity
    static class NoSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(CsrfConfigurer::disable)
                    .authorizeHttpRequests(auth ->
                            auth
                                    .requestMatchers("/v1/stores").authenticated()
                                    .anyRequest().authenticated())
                    .build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails admin = User.withUsername("manager")
                    .password("{noop}password")
                    .roles("MANAGER")
                    .build();

            UserDetails user = User.withUsername("owner")
                    .password("{noop}password")
                    .roles("OWNER")
                    .build();

            return new InMemoryUserDetailsManager(admin, user);
        }
    }
}