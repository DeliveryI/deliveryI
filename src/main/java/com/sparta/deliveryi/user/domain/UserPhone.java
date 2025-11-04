package com.sparta.deliveryi.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPhone {

    @Column(name = "phone_number", nullable = false, length = 11)
    private String value;

    private UserPhone(String number) {
        this.value = number;
    }

    public static UserPhone of(String number) {
        String normalized = number.replaceAll("\\D", "");

        if (!normalized.matches("^01[016]\\d{3,4}\\d{4}$")) {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다.");
        }

        return new UserPhone(normalized);
    }

    public String formatted() {
        if (value.length() == 10) {
            return value.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        } else if (value.length() == 11) {
            return value.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        } else {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다.");
        }
    }

    public String toString() {
        return this.value;
    }
}
