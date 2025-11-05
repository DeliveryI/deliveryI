package com.sparta.deliveryi.menu.infrastructure.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.menu.application.MenuVisibilityPolicy;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.QMenu;
import com.sparta.deliveryi.menu.domain.service.MenuFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class MenuFinderImpl implements MenuFinder {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Menu> findMenusByStore(
            UUID targetStoreId,
            UUID currentStoreId,
            String role,
            String menuName,
            Pageable pageable
    ) {
        QMenu m = QMenu.menu;
        BooleanBuilder builder = new BooleanBuilder().and(m.storeId.eq(targetStoreId));

        if (menuName != null && !menuName.isBlank()) {
            builder.and(m.menuName.containsIgnoreCase(menuName));
        }

        boolean isSameStore = Objects.equals(targetStoreId, currentStoreId);
        MenuVisibilityPolicy policy = getPolicyByRole(role);
        policy.apply(builder, new MenuVisibilityPolicy.VisibilityContext(m, isSameStore));

        List<Menu> contents = queryFactory
                .selectFrom(m)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(m.createdAt.desc())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(m.count()).from(m).where(builder).fetchOne()
        ).orElse(0L);

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Menu findById(Long menuId) {
        QMenu m = QMenu.menu;
        return queryFactory.selectFrom(m)
                .where(m.menuId.eq(menuId))
                .fetchOne();
    }

    private MenuVisibilityPolicy getPolicyByRole(String role) {
        try {
            return MenuVisibilityPolicy.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MenuVisibilityPolicy.CUSTOMER;
        }
    }
}
