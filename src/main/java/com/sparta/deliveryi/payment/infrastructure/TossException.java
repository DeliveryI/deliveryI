package com.sparta.deliveryi.payment.infrastructure;

import com.sparta.deliveryi.global.exception.AbstractException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TossException extends AbstractException {

    private final String code;
    private final String message;
    private final HttpStatus status;


    public TossException(String code, String message, HttpStatus status) {
        super(TossMessageCode.INTERNAL_FAILED);
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
