package com.sparta.deliveryi.global.exception;

import com.sparta.deliveryi.global.infrastructure.MessageResolver;
import lombok.Getter;

import java.io.Serial;

/**
 * 예외 처리용 추상 클래스.
 *
 * <pre>
 * 도메인별 커스텀 예외 클래스의 부모 클래스 역할을 하며,
 * 공통 메시지 코드 및 메시지 파라미터를 포함한 다양한 예외 생성자를 제공한다.
 * 예외 발생 시 코드 기반 메시지를 함께 처리할 수 있도록 한다.
 * </pre>
 *
 * @author 김형섭
 * @since 2025.10.30
 */
@Getter
public abstract class AbstractException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1021174412002879273L;

    /** 예외에 해당하는 메시지 코드 */
    private final transient MessageCode messageCode;

    /** 메시지 포맷에 사용될 매개변수 목록 */
    private final transient Object[] messageArguments;

    /**
     * 메시지, 원인, 메시지 매개변수를 포함한 예외 생성자.
     *
     * @param messageCode 메시지 코드
     * @param message 사용자 정의 메시지
     * @param cause 예외 원인
     * @param messageArguments 메시지 포맷 인자
     */
    protected AbstractException(final MessageCode messageCode, final String message, final Throwable cause, final Object... messageArguments){
        super(message, cause);
        this.messageCode = messageCode;
        this.messageArguments = messageArguments;
    }

    /**
     * 메시지와 원인을 포함한 예외 생성자.
     *
     * @param messageCode 메시지 코드
     * @param message 사용자 정의 메시지
     * @param cause 예외 원인
     */
    protected AbstractException(final MessageCode messageCode, final String message, final Throwable cause){
        super(message,cause);
        this.messageCode = messageCode;
        this.messageArguments = new Object[0];
    }

    /**
     * 메시지 코드, 원인, 메시지 포맷 인자를 포함한 예외 생성자.
     *
     * @param messageCode 메시지 코드
     * @param cause 예외 원인
     * @param messageArguments 메시지 포맷 인자
     */
    protected AbstractException(final MessageCode messageCode, final Throwable cause, final Object... messageArguments){
        super(MessageResolver.getMessage(messageCode.getCode(), messageArguments), cause);
        this.messageCode = messageCode;
        this.messageArguments = messageArguments;
    }

    /**
     * 메시지 코드와 원인을 포함한 예외 생성자.
     *
     * @param messageCode 메시지 코드
     * @param cause 예외 원인
     */
    protected AbstractException(final MessageCode messageCode, final Throwable cause){
        super(MessageResolver.getMessage(messageCode.getCode()), cause);
        this.messageCode = messageCode;
        this.messageArguments = new Object[0];
    }

    /**
     * 메시지 코드만 포함한 예외 생성자.
     *
     * @param messageCode 메시지 코드
     */
    protected AbstractException(final MessageCode messageCode){
        super(MessageResolver.getMessage(messageCode.getCode()));
        this.messageCode = messageCode;
        this.messageArguments = new Object[0];
    }

    /**
     * 메시지 코드와 메시지 포맷 인자를 포함한 예외 생성자.
     *
     * @param messageCode 메시지 코드
     * @param messageArguments 메시지 포맷 인자
     */
    protected AbstractException(final MessageCode messageCode, final Object... messageArguments){
        super(getMessage(messageCode, messageArguments));
        this.messageCode = messageCode;
        this.messageArguments = messageArguments;
    }

    /**
     * 메시지 생성 유틸리티.
     *
     * @param code 메시지 코드
     * @param messageArguments 메시지 포맷 인자
     * @return 생성된 메시지
     */
    private static String getMessage(MessageCode code, Object... messageArguments) {
        return MessageResolver.hasFormatParameters(code.getCode()) ? MessageResolver.getMessage(code.getCode(), messageArguments) : messageArguments[0].toString();
    }

}
