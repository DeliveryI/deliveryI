package com.sparta.deliveryi.menu.application.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deliveryi.menu.application.MenuVisibilityPolicy;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.QMenu;
import com.sparta.deliveryi.menu.domain.exception.MenuNotFoundException;
import com.sparta.deliveryi.menu.presentation.dto.MenuQueryResponse;
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

    public Page<MenuQueryResponse> getMenusByStore(
            String targetStoreId,
            String currentStoreId,
            String role,
            String menuName,
            int page, int size,
            String sortBy, String direction
    ) {
        QMenu m = QMenu.menu;
        BooleanBuilder builder = new BooleanBuilder()
                .and(m.storeId.eq(UUID.fromString(targetStoreId)));

        if (menuName != null && !menuName.isBlank()) {
            builder.and(m.menuName.containsIgnoreCase(menuName));
        }

        boolean isSameStore = currentStoreId != null && currentStoreId.equals(targetStoreId);

        // 역할별 정책 적용
        MenuVisibilityPolicy policy = MenuVisibilityPolicy.valueOf(role.toUpperCase());
        policy.apply(builder, new MenuVisibilityPolicy.VisibilityContext(m, isSameStore));

        Pageable pageable = createPageable(page, size, sortBy, direction);

        // QueryDSL 조회
        List<Menu> content = queryFactory.selectFrom(m)
                .where(builder)
                .orderBy(direction.equalsIgnoreCase("ASC") ? m.createdAt.asc() : m.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(
                queryFactory.select(m.count()).from(m).where(builder).fetchOne()
        ).orElse(0L);

        List<MenuQueryResponse> responses = content.stream()
                .map(MenuQueryResponse::from)
                .toList();

        return new PageImpl<>(responses, pageable, total);
    }

    public MenuQueryResponse getMenu(Long menuId) {
        QMenu m = QMenu.menu;
        Menu menu = queryFactory.selectFrom(m)
                .where(m.menuId.eq(menuId))
                .fetchOne();

        if (menu == null) {
            throw new MenuNotFoundException();
        }

        return MenuQueryResponse.from(menu);
    }

    // 페이지 생성 메서드
    private Pageable createPageable(int page, int size, String sortBy, String direction) {
        // 허용된 사이즈만 사용
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }
}
