package com.sparta.deliveryi.menu.presentation.dto;

import com.sparta.deliveryi.menu.domain.MenuStatus;

public record MenuRequest(
    String menuName,
    int menuPrice,
    MenuStatus menuStatus,
    String menuDescription,
    boolean aiGenerate,
    String prompt
) {}
