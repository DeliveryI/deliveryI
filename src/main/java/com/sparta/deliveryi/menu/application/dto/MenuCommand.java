package com.sparta.deliveryi.menu.application.dto;

import com.sparta.deliveryi.menu.domain.MenuStatus;

public record MenuCommand(
        String menuName,
        int menuPrice,
        MenuStatus menuStatus,
        String menuDescription,
        boolean aiGenerate,
        String prompt
) {}
