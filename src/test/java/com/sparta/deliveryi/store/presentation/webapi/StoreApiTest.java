package com.sparta.deliveryi.store.presentation.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.StoreRepository;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

import static com.sparta.deliveryi.AssertThatUtils.isTrue;
import static com.sparta.deliveryi.AssertThatUtils.notNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class StoreApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final StoreRepository storeRepository;

    @Autowired
    final StoreRegister storeRegister;

    @Test
    @WithMockUser
    void register() throws JsonProcessingException, UnsupportedEncodingException {
        StoreRegisterRequest request = StoreFixture.createStoreRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        System.out.println(result.getResponse().getContentAsString());

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue())
                .hasPathSatisfying("$.data", notNull());
    }

    @TestConfiguration
    static class NoSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(CsrfConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }
}