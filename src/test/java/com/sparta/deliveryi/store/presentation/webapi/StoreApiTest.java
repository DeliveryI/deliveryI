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
import static com.sparta.deliveryi.MockUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Import(DeliveryITestConfiguration.class)
@AutoConfigureMockMvc
class StoreApiTest {
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
    @WithMockUser(roles = "CUSTOMER")
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
    @WithMockUser(roles = "OWNER")
    void updateInfo() throws JsonProcessingException {
        StoreInfoUpdateRequest request = StoreFixture.createStoreUpdateRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        Store store = StoreFixture.createStore();
        given(
                storeApplication.updateInfo(
                        eq(storeId),
                        any(StoreInfoUpdateRequest.class),
                        eq(UUID.fromString(ownerId())))
        ).willReturn(store);

        mockedOwnerToken();

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
    @WithMockUser(roles = "CUSTOMER")
    void updateInfoIfUnauthorized() throws JsonProcessingException {
        StoreInfoUpdateRequest request = StoreFixture.createStoreUpdateRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        mockedCustomerToken();

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
    @WithMockUser(roles = "OWNER")
    void remove() {
        Store store = StoreFixture.createStore();
        given(
                storeApplication.remove(
                        eq(storeId),
                        eq(UUID.fromString(ownerId()))
                )
        ).willReturn(store);
        mockedOwnerToken();

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
    @WithMockUser(roles = "CUSTOMER")
    void removeIfUnauthorized() {
        mockedCustomerToken();

        MvcTestResult result = mvcTester.delete()
                .uri("/v1/stores/{storeId}", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void acceptRegisterRequest() {
        doNothing().when(storeRegister).acceptRegisterRequest(
                eq(storeId),
                any(UUID.class));

        mockedMangerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/accept", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void acceptRegisterRequestIfUnauthorized() {
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/accept", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void rejectRegisterRequest() {
        doNothing().when(storeRegister).rejectRegisterRequest(eq(storeId));

        mockedMangerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/reject", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void rejectRegisterRequestIfUnauthorized() {
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/reject", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void open() {
        doNothing().when(storeApplication).open(eq(storeId), eq(UUID.fromString(ownerId())));
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/open", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void openIfUnauthorized() {
        mockedCustomerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/open", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void close() {
        doNothing().when(storeApplication).close(eq(storeId), eq(UUID.fromString(ownerId())));
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/close", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void closeIfUnauthorized() {
        mockedCustomerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/stores/{storeId}/close", storeId.toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void transfer() throws JsonProcessingException {
        StoreTransferRequest request = new StoreTransferRequest(UUID.randomUUID());
        String requestJson = objectMapper.writeValueAsString(request);

        doNothing().when(storeApplication).transfer(eq(storeId), any(UUID.class), eq(UUID.fromString(ownerId())));
        mockedOwnerToken();

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
    @WithMockUser(roles = "CUSTOMER")
    void transferIfUnauthorized() throws JsonProcessingException {
        StoreTransferRequest request = new StoreTransferRequest(UUID.randomUUID());
        String requestJson = objectMapper.writeValueAsString(request);

        mockedCustomerToken();

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

}