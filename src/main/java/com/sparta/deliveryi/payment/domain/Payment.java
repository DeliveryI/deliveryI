package com.sparta.deliveryi.payment.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Table(name = "p_payment")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private UUID orderId;

    @Column
    private String paymentKey;

    @Column(nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus status;

    public static Payment create(UUID orderId, Integer totalPrice, String createdBy){
        validateTotalPrice(totalPrice);
        validateCreatedBy(createdBy);

        Payment payment = new Payment();

        payment.orderId = Objects.requireNonNull(orderId);
        payment.totalPrice = totalPrice;
        payment.status = PaymentStatus.PENDING;

        payment.createBy(createdBy);

        return payment;
    }

    public void updatePaymentKey(String paymentKey) {
        if (this.paymentKey != null && !this.paymentKey.isBlank()) {
            throw new PaymentException(PaymentMessageCode.PAYMENT_KEY_ALREADY_ASSIGNED);
        }

        validatePaymentKey(paymentKey);

        this.paymentKey = paymentKey;
    }

    public void approve() {
        if (this.status != PaymentStatus.PENDING) {
            throw new PaymentException(PaymentMessageCode.INVALID_STATUS_CHANGE, "결제 대기 상태가 아닙니다.");
        }
        this.status = PaymentStatus.APPROVED;
    }

    public void failed() {
        if (this.status != PaymentStatus.PENDING) {
            throw new PaymentException(PaymentMessageCode.INVALID_STATUS_CHANGE, "결제 대기 상태가 아닙니다.");
        }
        this.status = PaymentStatus.FAILED;
    }

    public void refunded() {
        if (this.status == PaymentStatus.PENDING) {
            throw new PaymentException(PaymentMessageCode.INVALID_STATUS_CHANGE, "결제가 완료된 상태에서만 환불할 수 있습니다.");
        }
        this.status = PaymentStatus.REFUNDED;
    }

    public void verifyAmount(int amount) {
        if (this.totalPrice != amount)
            throw new PaymentException(PaymentMessageCode.PAYMENT_AMOUNT_MISMATCH);
    }

    private static void validateTotalPrice(Integer totalPrice){
        if (totalPrice == null || totalPrice <= 0) {
            throw new IllegalArgumentException("결제 금액은 0원 이하일 수 없습니다.");
        }
    }

    private static void validateCreatedBy(String createdBy){
        if (createdBy == null || createdBy.isBlank()) {
            throw new IllegalArgumentException("결제 등록자(createdBy)는 비어 있을 수 없습니다.");
        }
    }

    private static void validatePaymentKey(String paymentKey){
        if (paymentKey == null || paymentKey.isBlank()) {
            throw new IllegalArgumentException("결제키(paymentKey)는 비어 있을 수 없습니다.");
        }
    }
}
