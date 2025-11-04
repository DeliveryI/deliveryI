package com.sparta.deliveryi.store.infrastructure;

import com.sparta.deliveryi.store.domain.AvailableAddress;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AvailableAddressConverterTest {

    AvailableAddressConverter converter = new AvailableAddressConverter();

    @Test
    void convertToDatabaseColumn() {
        List<String> addresses = List.of("강남구", "관악구", "강동구");

        String result = converter.convertToDatabaseColumn(AvailableAddress.from(addresses));

        assertThat(result).isEqualTo("강남구,관악구,강동구");
    }

    @Test
    void convertToDatabaseColumnIfEmptyList() {
        List<String> addresses = List.of();

        String result = converter.convertToDatabaseColumn(AvailableAddress.from(addresses));

        assertThat(result).isNull();
    }

    @Test
    void convertToDatabaseColumnIfNull() {
        String result = converter.convertToDatabaseColumn(null);

        assertThat(result).isNull();
    }

    @Test
    void convertToEntityAttribute() {
        String addresses = "강남구,관악구,강동구";

        AvailableAddress result = converter.convertToEntityAttribute(addresses);

        assertThat(result.getValues()).contains("강남구", "관악구", "강동구");
    }

    @Test
    void convertToEntityAttributeIfNull() {
        AvailableAddress result = converter.convertToEntityAttribute(null);

        assertThat(result).isNull();
    }

    @Test
    void convertToEntityAttributeIfBlank() {
        AvailableAddress result = converter.convertToEntityAttribute("");

        assertThat(result).isNull();
    }
}