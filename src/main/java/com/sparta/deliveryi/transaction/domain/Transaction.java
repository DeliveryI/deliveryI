package com.sparta.deliveryi.transaction.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "p_payment_transaction")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @EmbeddedId
    private TransactionId id;

    @Column(nullable = false)
    private Long paymentId;

    @Column(name = "transaction_amount", nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus status;

    @Embedded
    private TransactionResponse response;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @CreatedBy
    private String createBy;

    private void createBy(final String username) {
        this.createBy = username;
    }

    public static Transaction create(
            Long paymentId,
            Integer amount,
            TransactionType type,
            TransactionStatus status,
            TransactionResponse response,
            String username
    ) {
        validateAmount(amount);
        validateCreatedBy(username);

        Transaction transaction = new Transaction();

        transaction.id = TransactionId.generateId();
        transaction.paymentId = Objects.requireNonNull(paymentId);
        transaction.amount = amount;
        transaction.type = Objects.requireNonNull(type);
        transaction.status = Objects.requireNonNull(status);
        transaction.response = response;

        transaction.createBy(username);

        return transaction;
    }

    private static void validateAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("트랜잭션 금액은 0원 이하일 수 없습니다.");
        }
    }

    private static void validateCreatedBy(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("트랜잭션(결제) 등록자는 비어 있을 수 없습니다.");
        }
    }

    public void update() {
        throw new TransactionException(TransactionMessageCode.UPDATE_NOT_ALLOWED);
    }

    public void delete() {
        throw new TransactionException(TransactionMessageCode.DELETE_NOT_ALLOWED);
    }
}
