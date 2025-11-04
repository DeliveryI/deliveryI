package com.sparta.deliveryi.menu.domain;

import com.sparta.deliveryi.menu.domain.exception.MenuCreatedByEmptyException;
import com.sparta.deliveryi.menu.domain.exception.MenuPriceInvalidException;
import com.sparta.deliveryi.menu.domain.exception.MenuUpdatedByEmptyException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.sparta.deliveryi.global.domain.AbstractEntity;
import com.sparta.deliveryi.menu.domain.exception.*;

import java.util.UUID;

@Entity
@Table(name = "p_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "store_id", nullable = false)
    private UUID storeId;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "menu_price", nullable = false)
    private Integer menuPrice;

    @Column(name = "menu_description", nullable = false)
    private String menuDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_status", nullable = false)
    private MenuStatus menuStatus;

    // 생성 메서드
    public static Menu create(UUID storeId, String menuName, Integer menuPrice,
                              String menuDescription, MenuStatus menuStatus, String createdBy) {

        validatePrice(menuPrice);
        validateCreatedBy(createdBy);

        Menu menu = new Menu();
        menu.storeId = storeId;
        menu.menuName = menuName;
        menu.menuPrice = menuPrice;
        menu.menuDescription = menuDescription;
        menu.menuStatus = (menuStatus != null) ? menuStatus : MenuStatus.FORSALE;

        menu.createBy(createdBy);

        return menu;
    }

    // 수정 메서드
    public void update(String menuName, Integer menuPrice, String menuDescription,
                       MenuStatus menuStatus, String updatedBy) {

        validatePrice(menuPrice);
        validateUpdatedBy(updatedBy);

        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDescription = menuDescription;
        this.menuStatus = menuStatus;

        updateBy(updatedBy);
    }

    // 메뉴 상태 변경
    public void changeStatus(MenuStatus newStatus, String updatedBy) {
        if (this.isDeleted()) {
            throw new MenuDeletedException();
        }
        if (this.menuStatus == newStatus) {
            return;
        }
        this.menuStatus = newStatus;
        updateBy(updatedBy);
    }

    @Override
    public void delete() {
        super.delete();
    }

    // 비즈니스 규칙
    private static void validatePrice(Integer price) {
        if (price == null || price <= 0) {
            throw new MenuPriceInvalidException();
        }
    }

    private static void validateCreatedBy(String createdBy) {
        if (createdBy == null || createdBy.isBlank()) {
            throw new MenuCreatedByEmptyException();
        }
    }

    private static void validateUpdatedBy(String updatedBy) {
        if (updatedBy == null || updatedBy.isBlank()) {
            throw new MenuUpdatedByEmptyException();
        }
    }

    // 상태 확인
    public boolean isVisibleToCustomer() {
        return this.menuStatus != MenuStatus.HIDING;
    }

    public boolean isDeleted() {
        return this.getDeletedAt() != null;
    }
}
