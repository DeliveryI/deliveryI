package com.sparta.deliveryi.user.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.user.application.dto.TokenInfo;
import com.sparta.deliveryi.user.application.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.application.service.TokenGenerateService;
import com.sparta.deliveryi.user.application.service.UserRegister;
import com.sparta.deliveryi.user.domain.UserException;
import com.sparta.deliveryi.user.domain.UserMessageCode;
import com.sparta.deliveryi.user.presentation.dto.SignupReqeust;
import com.sparta.deliveryi.user.presentation.dto.TokenRequest;
import com.sparta.deliveryi.user.presentation.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.success;
import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name="회원 API", description = "")
public class UserApi {

    private final UserRegister userService;
    private final TokenGenerateService tokenService;

    @Operation(summary = "회원가입", description = "신규 사용자를 등록합니다. 가입 시 기본 권한은 'CUSTOMER' 입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupReqeust request) {
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

        return ok(success());
    }

    @Operation(summary = "인증 토큰 발급", description = "username, password 인증을 통해서 승인된 회원이 접근할 수 있는 토큰을 발급합니다.")
    @PostMapping("login")
    public ResponseEntity<ApiResponse<TokenResponse>> generateToken(@Valid @RequestBody TokenRequest request) {
        TokenInfo token = tokenService.generate(request.username(), request.password());
        TokenResponse response = TokenResponse.builder()
                .accessToken(token.access_token())
                .expiresIn(token.expires_in())
                .refreshExpiresIn(token.refresh_expires_in())
                .refreshToken(token.refresh_token())
                .tokenType(token.token_type())
                .build();

        return ok(successWithDataOnly(response));
    }
}
