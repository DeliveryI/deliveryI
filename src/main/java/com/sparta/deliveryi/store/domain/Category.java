package com.sparta.deliveryi.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Column(name = "store_category", nullable = false)
    private String name;

    private Category(String name) {
        this.name = name;
    }

    public static Category of(String name) {
        return new Category(name);
    }
}
