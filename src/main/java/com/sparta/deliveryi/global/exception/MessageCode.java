package com.sparta.deliveryi.global.exception;

import org.springframework.http.HttpStatus;

public interface MessageCode {
    String getCode();
    HttpStatus getStatus();
}
