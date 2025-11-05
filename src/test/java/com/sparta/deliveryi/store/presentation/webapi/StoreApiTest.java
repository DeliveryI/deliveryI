package com.sparta.deliveryi.store.presentation.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.DeliveryITestConfiguration;
import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.service.StoreRegisterService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sparta.deliveryi.AssertThatUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(DeliveryITestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class StoreApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final StoreRegisterService registerService;

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void register() throws JsonProcessingException {
        StoreRegisterRequest request = StoreFixture.createStoreRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        mockedToken(UUID.randomUUID().toString(), "CUSTOMER");

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
    @WithMockUser(username = "owner", roles = "OWNER")
    void updateInfo() throws JsonProcessingException {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());
        StoreInfoUpdateRequest request = StoreFixture.createStoreUpdateRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        mockedToken(store.getOwner().getId().toString(), "OWNER");

        MvcTestResult result = mvcTester.put()
                .uri("/v1/stores/{storeId}", store.getId().toString())
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
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void updateInfoIfUnauthorized() throws JsonProcessingException {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());
        StoreInfoUpdateRequest request = StoreFixture.createStoreUpdateRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        mockedToken(store.getOwner().getId().toString(), "CUSTOMER");

        MvcTestResult result = mvcTester.put()
                .uri("/v1/stores/{storeId}", store.getId().toString())
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
    @WithMockUser(username = "owner", roles = "OWNER")
    void remove() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());
        mockedToken(store.getOwner().getId().toString(), "OWNER");

        MvcTestResult result = mvcTester.delete()
                .uri("/v1/stores/{storeId}", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue())
                .hasPathSatisfying("$.data", notNull());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void removeIfUnauthorized() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());
        mockedToken(UUID.randomUUID().toString(), "CUSTOMER");

        MvcTestResult result = mvcTester.delete()
                .uri("/v1/stores/{storeId}", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
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

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void open() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());
        mockedToken(store.getOwner().getId().toString(), "OWNER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/open", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void openIfUnauthorized() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());
        mockedToken(UUID.randomUUID().toString(), "CUSTOMER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/open", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void close() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());
        mockedToken(store.getOwner().getId().toString(), "OWNER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/close", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void closeIfUnauthorized() {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());
        mockedToken(UUID.randomUUID().toString(), "CUSTOMER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/close", store.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void transfer() throws JsonProcessingException {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());

        StoreTransferRequest request = new StoreTransferRequest(store.getOwner().getId());
        String requestJson = objectMapper.writeValueAsString(request);

        mockedToken(store.getOwner().getId().toString(), "OWNER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/transfer", store.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void transferIfUnauthorized() throws JsonProcessingException {
        Store store = registerService.register(StoreFixture.createStoreRegisterRequest());
        registerService.acceptRegisterRequest(store.getId().toUuid());

        StoreTransferRequest request = new StoreTransferRequest(UUID.randomUUID());
        String requestJson = objectMapper.writeValueAsString(request);

        mockedToken(UUID.randomUUID().toString(), "CUSTOMER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/transfer", store.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
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