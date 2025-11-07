package com.sparta.deliveryi.payment.domain.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.global.infrastructure.QuerydslSortUtils;
import com.sparta.deliveryi.payment.application.dto.PaymentSearchRequest;
import com.sparta.deliveryi.payment.domain.Payment;
import com.sparta.deliveryi.payment.domain.PaymentRepositoryCustom;
import com.sparta.deliveryi.payment.domain.QPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Payment> search(PaymentSearchRequest search, Pageable pageable) {
        QPayment payment = QPayment.payment;

        BooleanBuilder condition = new BooleanBuilder();
        if (search.status() != null) {
            condition.and(payment.status.eq(search.status()));
        }

        OrderSpecifier<?>[] orders = QuerydslSortUtils.toOrderSpecifiers(Payment.class, "payment", "createdAt", pageable.getSort());

        List<Payment> content = queryFactory
                .selectFrom(payment)
                .where(condition)
                .orderBy(orders)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(
                queryFactory.select(payment.count()).from(payment).where(condition).fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}
