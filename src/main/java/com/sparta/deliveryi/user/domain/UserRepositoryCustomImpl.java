package com.sparta.deliveryi.user.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.global.infrastructure.QuerydslSortUtils;
import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> search(UserSearchRequest search, Pageable pageable) {
        QUser user = QUser.user;

        // 검색조건
        BooleanBuilder condition = new BooleanBuilder();
        if (search.username() != null && !search.username().isBlank()) {
            condition.and(user.username.containsIgnoreCase(search.username()));
        }
        if (search.nickname() != null && !search.nickname().isBlank()) {
            condition.and(user.nickname.containsIgnoreCase(search.nickname()));
        }
        if (search.role() != null) {
            condition.and(user.role.eq(search.role()));
        }

        OrderSpecifier<?>[] orders = QuerydslSortUtils.toOrderSpecifiers(User.class, "user", "createdAt", pageable.getSort());

        List<User> content = queryFactory
                .selectFrom(user)
                .where(condition)
                .orderBy(orders)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(
                queryFactory.select(user.count()).from(user).where(condition).fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}
