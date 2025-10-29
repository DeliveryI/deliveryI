package com.sparta.deliveryi.global;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ApiResponseTest {

    @Test
    void successWithMessageCodeAndData() {
        String messageCode = "SUCCESS_CODE";
        String expectedMessage = "성공했습니다";
        String data = "test data";

        try (MockedStatic<MessageResolver> mockedResolver = mockStatic(MessageResolver.class)) {
            mockedResolver.when(() -> MessageResolver.getMessage(messageCode))
                    .thenReturn(expectedMessage);

            ApiResponse<String> response = ApiResponse.success(messageCode, data);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessageCode()).isEqualTo(messageCode);
            assertThat(response.getMessage()).isEqualTo(expectedMessage);
            assertThat(response.getData()).isEqualTo(data);
        }
    }

    @Test
    void successWithDataOnly() {
        String data = "test data";

        ApiResponse<String> response = ApiResponse.successWithDataOnly(data);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessageCode()).isEmpty();
        assertThat(response.getMessage()).isEmpty();
        assertThat(response.getData()).isEqualTo(data);
    }

    @Test
    void successWithMessageCodeOnly() {
        String messageCode = "SUCCESS_CODE";
        String expectedMessage = "성공했습니다";

        try (MockedStatic<MessageResolver> mockedResolver = mockStatic(MessageResolver.class)) {
            mockedResolver.when(() -> MessageResolver.getMessage(messageCode))
                    .thenReturn(expectedMessage);

            ApiResponse<Void> response = ApiResponse.success(messageCode);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessageCode()).isEqualTo(messageCode);
            assertThat(response.getMessage()).isEqualTo(expectedMessage);
            assertThat(response.getData()).isNull();
        }
    }

    @Test
    void successDefault() {
        ApiResponse<Void> response = ApiResponse.success();

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessageCode()).isNull();
        assertThat(response.getMessage()).isEmpty();
        assertThat(response.getData()).isNull();
    }

    @Test
    void successWithMessageArgumentsWithData() {
        String messageCode = "SUCCESS_WITH_ARGS";
        String expectedMessage = "사용자 홍길동님이 등록되었습니다";
        String data = "user data";
        Object[] args = {"홍길동"};

        try (MockedStatic<MessageResolver> mockedResolver = mockStatic(MessageResolver.class)) {
            mockedResolver.when(() -> MessageResolver.getMessage(eq(messageCode), any()))
                    .thenReturn(expectedMessage);

            ApiResponse<String> response = ApiResponse.successWithMessageArguments(messageCode, data, args);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessageCode()).isEqualTo(messageCode);
            assertThat(response.getMessage()).isEqualTo(expectedMessage);
            assertThat(response.getData()).isEqualTo(data);
        }
    }

    @Test
    void successWithMessageArgumentsWithoutData() {
        String messageCode = "SUCCESS_WITH_ARGS";
        String expectedMessage = "5개의 항목이 처리되었습니다";
        Object[] args = {5};

        try (MockedStatic<MessageResolver> mockedResolver = mockStatic(MessageResolver.class)) {
            mockedResolver.when(() -> MessageResolver.getMessage(eq(messageCode), any()))
                    .thenReturn(expectedMessage);

            ApiResponse<Object> response = ApiResponse.successWithMessageArguments(messageCode, args);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessageCode()).isEqualTo(messageCode);
            assertThat(response.getMessage()).isEqualTo(expectedMessage);
            assertThat(response.getData()).isNull();
        }
    }

    @Test
    void failure_WithMessageCode() {
        String messageCode = "FAILURE_CODE";
        String expectedMessage = "처리에 실패했습니다";

        try (MockedStatic<MessageResolver> mockedResolver = mockStatic(MessageResolver.class)) {
            mockedResolver.when(() -> MessageResolver.getMessage(messageCode))
                    .thenReturn(expectedMessage);

            ApiResponse<Object> response = ApiResponse.failure(messageCode);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessageCode()).isEqualTo(messageCode);
            assertThat(response.getMessage()).isEqualTo(expectedMessage);
            assertThat(response.getData()).isNull();
        }
    }

    @Test
    void failureWithMessageArguments() {
        String messageCode = "FAILURE_WITH_ARGS";
        String expectedMessage = "ID 123을 찾을 수 없습니다";
        Object[] args = {123};

        try (MockedStatic<MessageResolver> mockedResolver = mockStatic(MessageResolver.class)) {
            mockedResolver.when(() -> MessageResolver.getMessage(eq(messageCode), any()))
                    .thenReturn(expectedMessage);

            ApiResponse<Object> response = ApiResponse.failureWithMessageArguments(messageCode, args);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessageCode()).isEqualTo(messageCode);
            assertThat(response.getMessage()).isEqualTo(expectedMessage);
            assertThat(response.getData()).isNull();
        }
    }

    @Test
    void toString_Test() {
        String messageCode = "TEST_CODE";
        String message = "테스트 메시지";
        String data = "test data";

        try (MockedStatic<MessageResolver> mockedResolver = mockStatic(MessageResolver.class)) {
            mockedResolver.when(() -> MessageResolver.getMessage(messageCode))
                    .thenReturn(message);

            ApiResponse<String> response = ApiResponse.success(messageCode, data);
            String result = response.toString();

            assertThat(result).contains("success=true");
            assertThat(result).contains("messageCode='TEST_CODE'");
            assertThat(result).contains("message='테스트 메시지'");
            assertThat(result).contains("data=test data");
        }
    }

    @Test
    void successWithEmptyMessageCode() {
        String data = "test data";

        ApiResponse<String> response = ApiResponse.success("", data);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessageCode()).isEmpty();
        assertThat(response.getMessage()).isEmpty();
        assertThat(response.getData()).isEqualTo(data);
    }

    @Test
    void successWithDifferentDataTypes() {
        ApiResponse<Integer> intResponse = ApiResponse.successWithDataOnly(100);
        assertThat(intResponse.getData()).isEqualTo(100);

        java.util.List<String> list = java.util.Arrays.asList("item1", "item2");
        ApiResponse<java.util.List<String>> listResponse = ApiResponse.successWithDataOnly(list);
        assertThat(listResponse.getData()).hasSize(2);

        class User {
            String name;
            User(String name) { this.name = name; }
        }
        User user = new User("홍길동");
        ApiResponse<User> userResponse = ApiResponse.successWithDataOnly(user);
        assertThat(userResponse.getData().name).isEqualTo("홍길동");
    }

    @Test
    void successWithNullData() {
        ApiResponse<String> response = ApiResponse.success(null);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isNull();
    }
}