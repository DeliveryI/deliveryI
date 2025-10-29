package com.sparta.deliveryi.global;

import org.springframework.http.HttpStatus;

public interface MessageCode {
    String getCode();
    HttpStatus getStatus();
}
