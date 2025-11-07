package com.sparta.deliveryi.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderId;
import com.sparta.deliveryi.order.domain.service.OrderFinder;
import com.sparta.deliveryi.payment.application.dto.*;
import com.sparta.deliveryi.payment.application.event.PaymentFailEvent;
import com.sparta.deliveryi.payment.application.event.PaymentSuccessEvent;
import com.sparta.deliveryi.payment.domain.Payment;
import com.sparta.deliveryi.payment.domain.PaymentException;
import com.sparta.deliveryi.payment.domain.PaymentMessageCode;
import com.sparta.deliveryi.payment.domain.PaymentStatus;
import com.sparta.deliveryi.payment.domain.service.PaymentQuery;
import com.sparta.deliveryi.payment.infrastructure.TossException;
import com.sparta.deliveryi.user.application.service.UserApplication;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import com.sparta.deliveryi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentApplicationService implements PaymentApplication {

    private final UserApplication userApplication;
    private final UserRolePolicy userRolePolicy;
    private final OrderFinder orderFinder;

    private final TossPaymentsService tossService;
    private final PaymentQuery paymentQuery;

    @Override
    public PaymentResponse confirm(UUID userId, PaymentConfirmCommand command) {
        Payment payment = paymentQuery.getPaymentByOrderId(command.orderId());
        User user = userApplication.getUserById(userId);

        // 데이터 검증
        userApplication.getUserById(userId);
        payment.verifyAmount(command.amount());

        // paymentKey 저장
        payment.updatePaymentKey(command.paymentKey());

        // 결제 승인 요청
        PaymentResponse response = tossService.confirm(command.paymentKey(), command.orderId().toString(), command.amount());

        if (response.httpStatus() == 200) {
            payment.approve(user.getUsername());
            Events.trigger(new PaymentSuccessEvent(command.orderId(), userId));
        } else {
            payment.failed(user.getUsername());
            Events.trigger(new PaymentFailEvent(command.orderId(), userId));

            HttpStatus httpStatus = HttpStatus.resolve(response.httpStatus());
            throw new TossException(response.code(), response.message(), httpStatus);
        }
        return response;
    }

    @Override
    public void fail(UUID userId, PaymentFailCommand command) {
        Payment payment = paymentQuery.getPaymentByOrderId(command.orderId());
        User user = userApplication.getUserById(userId);

        payment.failed(user.getUsername());
        Events.trigger(new PaymentFailEvent(command.orderId(), userId));
    }

    @Override
    public void refund(UUID orderId, int amount, String reason, UUID userId) {
        Payment payment = paymentQuery.getPaymentByOrderId(orderId);
        Order order = orderFinder.find(OrderId.of(orderId));

        // 결제 상태 검증 (APPROVE)
        if (payment.getStatus() != PaymentStatus.APPROVED) {
            throw new PaymentException(PaymentMessageCode.PAYMENT_NOT_APPROVED);
        }

        // amount = payment의 amount
        if (payment.getTotalPrice() != amount) {
            throw new PaymentException(PaymentMessageCode.PAYMENT_AMOUNT_MISMATCH);
        }

        // 결제자 = 주문자
        if (!order.getOrderer().getId().equals(userId)) {
            throw new PaymentException(PaymentMessageCode.PAYMENT_NOT_AUTHORIZED);
        }

        // 환불 요청
        PaymentCancelRequest request = new PaymentCancelRequest(reason);
        tossService.cancel(payment.getPaymentKey(), request);
    }

    @Override
    public Payment getPaymentByOrderId(UUID userId, UUID orderId) {
        Order order = orderFinder.find(OrderId.of(orderId));
        if (!order.getOrderer().getId().equals(userId)) {
            throw new PaymentException(PaymentMessageCode.PAYMENT_NOT_AUTHORIZED);
        }
        return paymentQuery.getPaymentByOrderId(orderId);
    }

    @Override
    public Page<Payment> searchPayments(UUID userId, PaymentSearchRequest search, Pageable pageable) {
        if(!userRolePolicy.isAdmin(userId)) {
            throw  new PaymentException(PaymentMessageCode.PAYMENT_NOT_AUTHORIZED);
        }

        return paymentQuery.getPayments(search, pageable);
    }
}
