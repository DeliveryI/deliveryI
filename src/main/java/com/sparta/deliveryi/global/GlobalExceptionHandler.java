package com.sparta.deliveryi.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 글로벌 예외 처리를 담당하는 컨트롤러 어드바이스 클래스.
 *
 * <pre>
 * - AbstractException 예외 처리 및 ApiResponse 변환
 * - 기타 예외 처리 및 로깅
 * </pre>
 *
 * @author 김형섭
 * @since 2025.10.30
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * AbstractException을 처리하여 실패 응답을 반환한다.
     *
     * @param e 처리할 AbstractException
     * @return ApiResponse<Void> 실패 응답
     */
    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<ApiResponse<Void>> globalException(AbstractException e) {
        if (e.getMessageArguments().length == 0) {
            ApiResponse<Void> failure = ApiResponse.failure(e.getMessageCode().getCode());
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            return new ResponseEntity<>(failure, e.getMessageCode().getStatus());
        }

        ApiResponse<Void> failure = ApiResponse.failureWithMessageArguments(e.getMessageCode().getCode(), e.toString(), e.getMessageArguments());
        log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(failure, e.getMessageCode().getStatus());
    }

    /**
     * 그 외 모든 예외를 처리하여 UNKNOWN 응답을 반환한다.
     *
     * @param e 처리할 예외
     * @return ApiResponse<Void> UNKNOWN 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> exception(Exception e) {
        ApiResponse<Void> failure = ApiResponse.failure(GlobalMessageCode.UNKNOWN.getCode());
        log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(failure, GlobalMessageCode.UNKNOWN.getStatus());
    }
}
