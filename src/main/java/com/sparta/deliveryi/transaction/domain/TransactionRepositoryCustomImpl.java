package com.sparta.deliveryi.transaction.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.global.infrastructure.QuerydslSortUtils;
import com.sparta.deliveryi.payment.domain.PaymentRepository;
import com.sparta.deliveryi.transaction.application.dto.TransactionSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final PaymentRepository paymentRepository;

    @Override
    public Page<Transaction> search(TransactionSearchRequest search, Pageable pageable) {
        QTransaction transaction = QTransaction.transaction;

        // 검색조건
        BooleanBuilder condition = new BooleanBuilder();
        if (search.orderId() != null) {
            paymentRepository.findByOrderId(search.orderId())
                    .ifPresent(payment ->
                        condition.and(transaction.paymentId.eq(payment.getId()))
                    );
        }
        if (search.type() != null) {
            condition.and(transaction.type.eq(search.type()));
        }
        if (search.status() != null) {
            condition.and(transaction.status.eq(search.status()));
        }
        if (search.username() != null && !search.username().isEmpty()) {
            condition.and(transaction.createBy.contains(search.username()));
        }

        OrderSpecifier<?>[] orders = QuerydslSortUtils.toOrderSpecifiers(Transaction.class, "transaction", "createdAt", pageable.getSort());

        List<Transaction> content = queryFactory
                .selectFrom(transaction)
                .where(condition)
                .orderBy(orders)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(
                queryFactory.select(transaction.count()).from(transaction).where(condition).fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}
