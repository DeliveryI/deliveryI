package com.sparta.deliveryi.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvailableAddress {

    @Column(name = "available_address")
    private List<String> values;

    private AvailableAddress(List<String> values) {
        this.values = values == null ? Collections.emptyList() : List.copyOf(values);
    }

    public static AvailableAddress from(List<String> values) {
        return new AvailableAddress(values);
    }

    public List<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    public boolean isEmpty() {
        return values == null || values.isEmpty();
    }
}
