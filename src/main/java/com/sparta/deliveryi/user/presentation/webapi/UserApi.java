package com.sparta.deliveryi.user.presentation.webapi;

import com.sparta.deliveryi.user.application.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.application.service.UserRegister;
import com.sparta.deliveryi.user.domain.UserException;
import com.sparta.deliveryi.user.domain.UserMessageCode;
import com.sparta.deliveryi.user.presentation.dto.SignupReqeust;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name="회원 API", description = "")
public class UserApi {

    private final UserRegister userService;

    @Operation(summary = "회원가입", description = "신규 사용자를 등록합니다. 가입 시 기본 권한은 'CUSTOMER' 입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("signup")
    public void signup(@Valid @RequestBody SignupReqeust request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new UserException(UserMessageCode.PASSWORD_MISMATCH);
        }

        UserRegisterRequest registerRequest = UserRegisterRequest.builder()
                .username(request.username())
                .password(request.password())
                .nickname(request.nickname())
                .userPhone(request.userPhone())
                .currentAddress(request.currentAddress())
                .build();

        userService.register(registerRequest);
    }
}
