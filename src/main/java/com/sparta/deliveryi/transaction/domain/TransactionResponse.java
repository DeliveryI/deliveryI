package com.sparta.deliveryi.transaction.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TransactionResponse {
    @Column(name = "transaction_key")
    private String transactionKey;

    @Column(name = "response_code")
    private String code;

    @Column(name = "response_message")
    private String message;
}

