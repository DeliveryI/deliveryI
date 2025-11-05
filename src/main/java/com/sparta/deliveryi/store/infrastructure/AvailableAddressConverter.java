package com.sparta.deliveryi.store.infrastructure;

import com.sparta.deliveryi.store.domain.AvailableAddress;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

@Converter(autoApply = true)
public class AvailableAddressConverter implements AttributeConverter<AvailableAddress, String> {
    @Override
    public String convertToDatabaseColumn(AvailableAddress addresses) {
        return ObjectUtils.isEmpty(addresses) || ObjectUtils.isEmpty(addresses.getValues())
                ? null : String.join(",", addresses.getValues());
    }

    @Override
    public AvailableAddress convertToEntityAttribute(String addresses) {
        return ObjectUtils.isEmpty(addresses) ? null : AvailableAddress.from(Arrays.asList(addresses.split(",")));
    }
}
