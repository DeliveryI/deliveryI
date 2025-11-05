package com.sparta.deliveryi.user.presentation.validator;

import com.sparta.deliveryi.user.presentation.annotation.ValidPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String number, ConstraintValidatorContext constraintValidatorContext) {
        number = number.replaceAll("\\D", "");
        String pattern = "^01[016]\\d{3,4}\\d{4}$";
        return number.matches(pattern);
    }
}
