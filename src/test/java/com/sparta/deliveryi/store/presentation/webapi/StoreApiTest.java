package com.sparta.deliveryi.store.presentation.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.DeliveryITestConfiguration;
import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.application.service.StoreApplication;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
import com.sparta.deliveryi.store.domain.service.StoreRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.List;
import java.util.UUID;

import static com.sparta.deliveryi.AssertThatUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Import(DeliveryITestConfiguration.class)
@AutoConfigureMockMvc
class StoreApiTest {
    static final String MANAGER_UUID = "00000000-0000-0000-0000-000000000000";
    static final String OWNER_UUID = "11111111-1111-1111-1111-111111111111";
    static final String CUSTOMER_UUID = "22222222-2222-2222-2222-222222222222";
    UUID storeId = UUID.randomUUID();

    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    StoreRegisterService registerService;

    @MockitoBean
    StoreApplication storeApplication;
    @Autowired
    private StoreRegister storeRegister;

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void register() throws JsonProcessingException {
        StoreRegisterRequest request = StoreFixture.createStoreRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        Store store = StoreFixture.createStore();
        given(registerService.register(any(StoreRegisterRequest.class))).willReturn(store);

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
        StoreInfoUpdateRequest request = StoreFixture.createStoreUpdateRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        Store store = StoreFixture.createStore();
        given(
                storeApplication.updateInfo(
                        eq(storeId),
                        any(StoreInfoUpdateRequest.class),
                        eq(UUID.fromString(OWNER_UUID)))
        ).willReturn(store);

        mockedToken(OWNER_UUID, "OWNER");

        MvcTestResult result = mvcTester.put()
                .uri("/v1/stores/{storeId}", storeId.toString())
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
        StoreInfoUpdateRequest request = StoreFixture.createStoreUpdateRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        mockedToken(CUSTOMER_UUID, "CUSTOMER");

        MvcTestResult result = mvcTester.put()
                .uri("/v1/stores/{storeId}", storeId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void remove() {
        Store store = StoreFixture.createStore();
        given(
                storeApplication.remove(
                        eq(storeId),
                        eq(UUID.fromString(OWNER_UUID))
                )
        ).willReturn(store);
        mockedToken(OWNER_UUID, "OWNER");

        MvcTestResult result = mvcTester.delete()
                .uri("/v1/stores/{storeId}", storeId.toString())
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
        mockedToken(CUSTOMER_UUID, "CUSTOMER");

        MvcTestResult result = mvcTester.delete()
                .uri("/v1/stores/{storeId}", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void acceptRegisterRequest() {
        doNothing().when(storeRegister).acceptRegisterRequest(
                eq(storeId),
                any(UUID.class));

        mockedToken(UUID.randomUUID().toString(), "MANAGER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/accept", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void acceptRegisterRequestIfUnauthorized() {
        mockedToken(OWNER_UUID, "OWNER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/accept", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void rejectRegisterRequest() {
        doNothing().when(storeRegister).rejectRegisterRequest(eq(storeId));

        mockedToken(UUID.randomUUID().toString(), "MANAGER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/reject", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void rejectRegisterRequestIfUnauthorized() {
        mockedToken(OWNER_UUID, "OWNER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/reject", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void open() {
        doNothing().when(storeApplication).open(eq(storeId), eq(UUID.fromString(OWNER_UUID)));
        mockedToken(OWNER_UUID, "OWNER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/open", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void openIfUnauthorized() {
        mockedToken(UUID.randomUUID().toString(), "CUSTOMER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/open", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void close() {
        doNothing().when(storeApplication).close(eq(storeId), eq(UUID.fromString(OWNER_UUID)));
        mockedToken(OWNER_UUID, "OWNER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/close", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void closeIfUnauthorized() {
        mockedToken(UUID.randomUUID().toString(), "CUSTOMER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/close", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(username = "owner", roles = "OWNER")
    void transfer() throws JsonProcessingException {
        StoreTransferRequest request = new StoreTransferRequest(UUID.randomUUID());
        String requestJson = objectMapper.writeValueAsString(request);

        doNothing().when(storeApplication).transfer(eq(storeId), any(UUID.class), eq(UUID.fromString(OWNER_UUID)));
        mockedToken(OWNER_UUID, "OWNER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/transfer", storeId.toString())
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
        StoreTransferRequest request = new StoreTransferRequest(UUID.randomUUID());
        String requestJson = objectMapper.writeValueAsString(request);

        mockedToken(UUID.randomUUID().toString(), "CUSTOMER");

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/transfer", storeId.toString())
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