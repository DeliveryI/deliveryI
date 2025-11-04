package com.sparta.deliveryi.user.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> search(UserSearchRequest search, Pageable pageable) {
        QUser user = QUser.user;

        // 검색조건
        BooleanBuilder builder = new BooleanBuilder();
        if (search.username() != null && !search.username().isBlank()) {
            builder.and(user.username.containsIgnoreCase(search.username()));
        }
        if (search.nickname() != null && !search.nickname().isBlank()) {
            builder.and(user.nickname.containsIgnoreCase(search.nickname()));
        }
        if (search.role() != null) {
            builder.and(user.role.eq(search.role()));
        }

        List<User> content = queryFactory
                .selectFrom(user)
                .where(builder)
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(
                queryFactory.select(user.count()).from(user).where(builder).fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        PathBuilder<User> pathBuilder = new PathBuilder<>(User.class, "user");

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            orders.add(new OrderSpecifier<>(direction, pathBuilder.getString(order.getProperty())));
        }

        orders.add(new OrderSpecifier<>(Order.DESC, pathBuilder.getDateTime("createdAt", LocalDateTime.class)));

        return orders.toArray(new OrderSpecifier[0]);
    }
}
