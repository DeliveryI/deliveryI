package com.sparta.deliveryi.menu.application.dto;

import com.sparta.deliveryi.menu.domain.MenuStatus;

public record MenuStatusChangeCommand(
        Long menuId,
        MenuStatus status
) {}
