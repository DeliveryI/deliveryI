package com.sparta.deliveryi.menu.application;

import com.querydsl.core.BooleanBuilder;
import com.sparta.deliveryi.menu.domain.QMenu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuVisibilityPolicy 단위 테스트")
class MenuVisibilityPolicyTest {

    private final QMenu m = QMenu.menu;

    @Test
    @DisplayName("CUSTOMER 정책은 HIDING 상태와 삭제된 메뉴를 제외해야 한다")
    void customerPolicy_excludesHiddenAndDeleted() {
        BooleanBuilder builder = new BooleanBuilder();

        MenuVisibilityPolicy policy = MenuVisibilityPolicy.CUSTOMER;
        policy.apply(builder, new MenuVisibilityPolicy.VisibilityContext(m, false));

        String result = builder.getValue().toString();

        assertThat(result)
                .contains("menu.menuStatus != HIDING")
                .contains("menu.deletedAt is null");
    }

    @Test
    @DisplayName("OWNER 정책: 같은 가게면 삭제되지 않은 메뉴만 표시")
    void ownerPolicy_sameStore() {
        BooleanBuilder builder = new BooleanBuilder();

        MenuVisibilityPolicy policy = MenuVisibilityPolicy.OWNER;
        policy.apply(builder, new MenuVisibilityPolicy.VisibilityContext(m, true));

        String result = builder.getValue().toString();

        assertThat(result)
                .doesNotContain("menu.menuStatus")
                .contains("menu.deletedAt is null");
    }

    @Test
    @DisplayName("OWNER 정책: 다른 가게면 HIDING 제외, 삭제 안 된 메뉴만 표시")
    void ownerPolicy_differentStore() {
        BooleanBuilder builder = new BooleanBuilder();
        MenuVisibilityPolicy.OWNER.apply(builder, new MenuVisibilityPolicy.VisibilityContext(m, false));

        String result = builder.getValue().toString();

        assertThat(result)
                .contains("menu.menuStatus != HIDING")
                .contains("menu.deletedAt is null");
    }

    @Test
    @DisplayName("MANAGER 정책은 모든 메뉴를 표시 (조건 없음)")
    void managerPolicy_noRestrictions() {
        BooleanBuilder builder = new BooleanBuilder();

        MenuVisibilityPolicy.MANAGER.apply(builder, new MenuVisibilityPolicy.VisibilityContext(m, false));

        assertThat(builder.hasValue()).isFalse();
    }

    @Test
    @DisplayName("MASTER 정책은 모든 메뉴를 표시 (조건 없음)")
    void masterPolicy_noRestrictions() {
        BooleanBuilder builder = new BooleanBuilder();

        MenuVisibilityPolicy.MASTER.apply(builder, new MenuVisibilityPolicy.VisibilityContext(m, false));

        assertThat(builder.hasValue()).isFalse();
    }
}
