package com.sparta.deliveryi.user.domain;

public enum UserRole {
    CUSTOMER, OWNER, MANAGER, MASTER;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
