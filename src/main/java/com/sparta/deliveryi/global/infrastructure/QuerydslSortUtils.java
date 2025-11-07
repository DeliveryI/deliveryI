package com.sparta.deliveryi.global.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuerydslSortUtils {
    /**
     * 기본 정렬 기준만 적용
     *
     * @param entityClass 정렬 대상 엔티티 클래스
     * @param alias Q클래스의 별칭 (예: "user", "payment")
     * @param defaultSortField 기본 정렬 필드명 (예: "createdAt")
     * @return OrderSpecifier 배열
     */
    public static <T> OrderSpecifier<?>[] toOrderSpecifiers(Class<T> entityClass, String alias, String defaultSortField) {
        return toOrderSpecifiers(entityClass, alias, defaultSortField, Sort.unsorted());
    }

    /**
     * Pageable의 정렬 정보와 기본 정렬 기준 적용
     */
    public static <T> OrderSpecifier<?>[] toOrderSpecifiers(Class<T> entityClass, String alias, String defaultSortField, Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        PathBuilder<T> pathBuilder = new PathBuilder<>(entityClass, alias);

        // 요청된 정렬 정보 추가
        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            orders.add(new OrderSpecifier<>(direction, pathBuilder.getString(order.getProperty())));
        }

        // 기본 정렬 (ex. createdAt DESC)
        orders.add(new OrderSpecifier<>(Order.DESC, pathBuilder.getDateTime(defaultSortField, LocalDateTime.class)));

        return orders.toArray(new OrderSpecifier[0]);
    }
}
