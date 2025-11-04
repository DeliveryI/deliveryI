package com.sparta.deliveryi.menu.application;

import com.querydsl.core.BooleanBuilder;
import com.sparta.deliveryi.menu.domain.MenuStatus;
import com.sparta.deliveryi.menu.domain.QMenu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@Getter
@RequiredArgsConstructor
public enum MenuVisibilityPolicy {

    CUSTOMER((builder, ctx) -> builder
            .and(ctx.m.menuStatus.ne(MenuStatus.HIDING))
            .and(ctx.m.deletedAt.isNull())),

    OWNER((builder, ctx) -> {
        if (ctx.isSameStore()) {
            builder.and(ctx.m.deletedAt.isNull());
        } else {
            builder.and(ctx.m.menuStatus.ne(MenuStatus.HIDING))
                   .and(ctx.m.deletedAt.isNull());
        }
    }),

    MANAGER((builder, ctx) -> {}),
    MASTER((builder, ctx) -> {});

    private final BiConsumer<BooleanBuilder, VisibilityContext> rule;

    public void apply(BooleanBuilder builder, VisibilityContext ctx) {
        rule.accept(builder, ctx);
    }

    public record VisibilityContext(QMenu m, boolean isSameStore) {}
}
