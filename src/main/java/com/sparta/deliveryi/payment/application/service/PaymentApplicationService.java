package com.sparta.deliveryi.payment.application.service;

import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.payment.application.dto.PaymentConfirmCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentFailCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentResponse;
import com.sparta.deliveryi.payment.application.event.PaymentFailEvent;
import com.sparta.deliveryi.payment.application.event.PaymentSuccessEvent;
import com.sparta.deliveryi.payment.domain.Payment;
import com.sparta.deliveryi.payment.domain.service.PaymentQuery;
import com.sparta.deliveryi.payment.infrastructure.TossException;
import com.sparta.deliveryi.user.application.service.UserApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentApplicationService implements PaymentApplication {

    private final UserApplication userApplication;

    private final TossPaymentsService tossService;
    private final PaymentQuery paymentQuery;

    @Override
    public PaymentResponse confirm(UUID userId, PaymentConfirmCommand command) {
        Payment payment = paymentQuery.getPaymentByOrderId(command.orderId());

        // 데이터 검증
        userApplication.getUserById(userId);
        payment.verifyAmount(command.amount());

        // paymentKey 저장
        payment.updatePaymentKey(command.paymentKey());

        // 결제 승인 요청
        PaymentResponse response = tossService.confirm(command.paymentKey(), command.orderId().toString(), command.amount());

        if (response.httpStatus() == 200) {
            payment.approve();
            Events.trigger(new PaymentSuccessEvent(command.orderId(), userId));
        } else {
            payment.failed();
            Events.trigger(new PaymentFailEvent(command.orderId(), userId));

            HttpStatus httpStatus = HttpStatus.resolve(response.httpStatus());
            throw new TossException(response.code(), response.message(), httpStatus);
        }
        return response;
    }

    @Override
    public void fail(UUID userId, PaymentFailCommand command) {
        Payment payment = paymentQuery.getPaymentByOrderId(command.orderId());

        payment.failed();
        Events.trigger(new PaymentFailEvent(command.orderId(), userId));
    }
}
