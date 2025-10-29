package com.sparta.deliveryi.global;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiResponse<T> {
    private final boolean success;  // 요청에 대한 성공 여부
    private final String messageCode;  // 응답 메세지 코드
    private final String message;  // 응답 코드에 해당하는 응답 메세지
    private final T data;  // 응답 데이터

    /**
     * 메세지 코드, 데이터를 포함하는 성공 응답 메서드.
     *
     * @param <T> 응답 데이터의 타입
     * @param messageCode 응답 메세지 코드
     * @param data 응답 데이터
     * @return 성공 응답을 나타내는 {@code ApiResponse}
     */
    public static <T> ApiResponse<T> success(final String messageCode, final T data) {
        return success(messageCode, data);
    }

    /**
     * 메세지 코드, 데이터를 포함하는 성공 응답 메서드.
     *
     * @param <T> 응답 데이터의 타입
     * @param messageCode 응답 메세지 코드
     * @param data 응답 데이터
     * @param messageArguments 응답 메세지에 필요한 매개변수
     * @return 성공 응답을 나타내는 {@code ApiResponse}
     */
    public static <T> ApiResponse<T> successWithMessageArguments(final String messageCode, final T data, final Object... messageArguments) {
        return successWithMessageArguments(messageCode, data, messageArguments);
    }

    /**
     * 데이터를 포함하는 성공 응답 메서드.
     *
     * @param <T> 응답 데이터의 타입
     * @param data 응답 데이터
     * @return 성공 응답을 나타내는 {@code ApiResponse}
     */
    public static <T> ApiResponse<T> success(final T data) {
        return success(data);
    }

    /**
     * 메세지 코드를 포함하는 성공 응답 메서드.
     *
     * @param <T> 응답 데이터의 타입
     * @param messageCode 응답 메세지 코드
     * @return 성공 응답을 나타내는 {@code ApiResponse}
     */
    public static <T> ApiResponse<T> success(final String messageCode) {
        return success(messageCode, null);
    }

    /**
     * 메세지 코드를 포함하는 성공 응답 메서드.
     *
     * @param <T> 응답 데이터의 타입
     * @param messageCode 응답 메세지 코드
     * @return 성공 응답을 나타내는 {@code ApiResponse}
     */
    public static <T> ApiResponse<T> successWithMessageArguments(final String messageCode, final Object... messageArguments) {
        return successWithMessageArguments(messageCode, null, messageArguments);
    }

    /**
     * 기본 성공 응답 메서드.
     *
     * @return 성공 응답을 나타내는 {@code ApiResponse}
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 메세지 코드를 포함하는 실패 응답 메서드.
     *
     * @param <T> 응답 데이터의 타입
     * @param messageCode 응답 메세지 코드
     * @return 성공 응답을 나타내는 {@code ApiResponse}
     */
    public static <T> ApiResponse<T> failure(final String messageCode) {
        return failure(messageCode);
    }

    /**
     * 메세지 코드를 포함하는 실패 응답 메서드.
     *
     * @param <T> 응답 데이터의 타입
     * @param messageCode 응답 메세지 코드
     * @param messageArguments 응답 메세지에 필요한 매개변수
     * @return 성공 응답을 나타내는 {@code ApiResponse}
     */
    public static <T> ApiResponse<T> failureWithMessageArguments(final String messageCode, final Object... messageArguments) {
        return failureWithMessageArguments(messageCode, messageArguments);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", messageCode='" + messageCode + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 메세지 코드에 해당하는 메세지를 가져오는 메서드.
     *
     * @param messageCode 메세지 코드
     * @return 메세지 코드에 해당하는 메세지
     */
    private static String getMessage(final String messageCode) {
        String message = "";
        if (StringUtils.isNotBlank(messageCode)) {
            message = MessageResolver.getMessage(messageCode);
        }
        return message;
    }

    /**
     * 메세지 코드에 해당하는 메세지를 가져오는 메서드.
     *
     * @param messageCode 메세지 코드
     * @param messageArguments 응답 메세지에 필요한 매개변수
     * @return 메세지 코드에 해당하는 메세지
     */
    private static String getMessage(final String messageCode, final Object... messageArguments) {
        String message = "";
        if (StringUtils.isNotBlank(messageCode)) {
            message = MessageResolver.getMessage(messageCode, messageArguments);
        }
        return message;
    }
}
