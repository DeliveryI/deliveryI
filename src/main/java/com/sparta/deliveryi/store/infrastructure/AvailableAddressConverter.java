package com.sparta.deliveryi.store.infrastructure;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@Converter
public class AvailableAddressConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> addresses) {
        return ObjectUtils.isEmpty(addresses) ? null : String.join(",", addresses);
    }

    @Override
    public List<String> convertToEntityAttribute(String addresses) {
        return ObjectUtils.isEmpty(addresses) ? null : Arrays.asList(addresses.split(","));
    }
}
