package com.sparta.deliveryi.user.presentation.validator;

import com.sparta.deliveryi.user.presentation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    // 대소문자 구분 없이 알파벳 1개 이상, 숫자 1개 이상, 특수문자 1개 이상
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        boolean hasAlpha = checkAlpha(password, true);
        boolean hasNumber = checkNumber(password);
        boolean hasSpecial = checkSpecialChars(password);

        return hasAlpha && hasNumber && hasSpecial;
    }

     boolean checkAlpha(String password, boolean caseInsensitive) {
        if (caseInsensitive) { // 대소문자 구분없이 알파벳 1자 이상
            // .* : 0이상 아무 문자  [a-zA-Z]+ : 알파벳 대소문자 상관없이 1자 이상
            return password.matches(".*[a-zA-Z]+.*");
        }

        // 대문자 1개 이상, 소문자 1개 이상
        return password.matches(".*[a-z]+.*") && password.matches(".*[A-Z]+.*");
     }

     boolean checkNumber(String password) {
        return password.matches(".*\\d+.*");
    }

     boolean checkSpecialChars(String password) {
        String pattern = ".*[^0-9a-zA-Zㄱ-ㅎ가-힣]+.*";  // 숫자, 알파벳, 한글을 제외한 모든 문자(특수문자)

        return password.matches(pattern);
    }
}
