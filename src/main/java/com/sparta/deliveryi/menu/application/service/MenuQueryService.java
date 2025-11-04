package com.sparta.deliveryi.menu.application.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.menu.application.MenuVisibilityPolicy;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.QMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuQueryService {

    private final JPAQueryFactory queryFactory;

    public Page<Menu> getMenusByStore(
            String targetStoreId,
            String currentStoreId,
            String role,
            String menuName,
            int page, int size,
            String sortBy, String direction
    ) {
        QMenu m = QMenu.menu;
        BooleanBuilder builder = new BooleanBuilder();

        try {
            UUID storeUuid = UUID.fromString(targetStoreId);
            builder.and(m.storeId.eq(storeUuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 storeId 형식입니다: " + targetStoreId);
        }

        if (menuName != null && !menuName.isBlank()) {
            builder.and(m.menuName.containsIgnoreCase(menuName));
        }

        boolean isSameStore = Objects.equals(targetStoreId, currentStoreId);
        MenuVisibilityPolicy policy = getPolicyByRole(role);
        policy.apply(builder, new MenuVisibilityPolicy.VisibilityContext(m, isSameStore));

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

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

    private MenuVisibilityPolicy getPolicyByRole(String role) {
        try {
            return MenuVisibilityPolicy.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MenuVisibilityPolicy.CUSTOMER;
        }
    }

    public Menu getMenu(Long menuId) {
        QMenu m = QMenu.menu;
        return queryFactory.selectFrom(m)
                .where(m.menuId.eq(menuId))
                .fetchOne();
    }
}
