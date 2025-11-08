package com.sparta.deliveryi.user.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.user.presentation.dto.LoginUserInfoResponse;
import com.sparta.deliveryi.user.application.dto.TokenInfo;
import com.sparta.deliveryi.user.presentation.dto.UserInfoResponse;
import com.sparta.deliveryi.user.application.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.application.service.TokenGenerateService;
import com.sparta.deliveryi.user.application.service.UserApplication;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserException;
import com.sparta.deliveryi.user.domain.UserMessageCode;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import com.sparta.deliveryi.user.presentation.dto.SignupReqeust;
import com.sparta.deliveryi.user.presentation.dto.TokenRequest;
import com.sparta.deliveryi.user.presentation.dto.TokenResponse;
import com.sparta.deliveryi.user.presentation.dto.UserInfoChangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.success;
import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "회원 API", description = "회원 가입, 로그인, 정보조회 및 수정, 탈퇴 기능 제공")
public class UserApi {

    private final TokenGenerateService tokenService;
    private final UserApplication userApplication;

    @Operation(
            summary = "회원가입",
            description = "신규 회원을 등록합니다. 가입 시 기본 권한은 'CUSTOMER' 입니다.",
            security = @SecurityRequirement(name = "")
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupReqeust request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new UserException(UserMessageCode.CONFIRM_PASSWORD_MISMATCH);
        }

        UserRegisterRequest registerRequest = UserRegisterRequest.builder()
                .username(request.username())
                .password(request.password())
                .nickname(request.nickname())
                .userPhone(request.userPhone())
                .currentAddress(request.currentAddress())
                .build();

        userApplication.register(registerRequest);

        return ok(success());
    }

    @Operation(
            summary = "로그인",
            description = "username, password 인증을 통해서 승인된 회원이 접근할 수 있는 토큰을 발급합니다."
    )
    @PostMapping("/login")
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

    @Operation(
            summary = "로그아웃",
            description = """
                    Keycloak 회원 세션에서 로그아웃 처리하며, RefreshToken을 무효화합니다.
                    이미 발급된 AccessToken은 블랙리스트에 저장하여 Security Filter로 검증합니다.
            """
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal Jwt jwt) {
        userApplication.logout(UUID.fromString(jwt.getSubject()), jwt.getTokenValue(), jwt.getExpiresAt());

        return ok(success());
    }

    @Operation(
            summary = "로그인한 회원 정보 조회",
            description = "JWT를 기반으로 현재 로그인한 회원의 정보를 반환합니다."
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<ApiResponse<LoginUserInfoResponse>> getMyInfo(@AuthenticationPrincipal Jwt jwt) {
        User response = userApplication.getLoginUser(UUID.fromString(jwt.getSubject()));

        return ok(successWithDataOnly(LoginUserInfoResponse.from(response)));
    }

    @Operation(
            summary = "특정 회원 정보 조회",
            description = """
                    UserId로 특정 회원의 정보를 조회합니다.
                    username, userPhone, currentAddress 같은 민감한 정보는 제외됩니다.
            """
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(
            @Parameter(
                    name = "userId",
                    description = "조회할 회원의 ID (UUID)",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable UUID userId
    ) {
        User response = userApplication.getUserById(userId);

        return ok(successWithDataOnly(UserInfoResponse.from(response)));
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "로그인한 회원의 nickname, userPhone, currentAddress를 수정합니다."
    )
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> changeMyInfo(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserInfoChangeRequest request
    ) {
        UserInfoUpdateRequest updateRequest = UserInfoUpdateRequest.builder()
                .nickname(request.nickname())
                .userPhone(request.userPhone())
                .currentAddress(request.currentAddress())
                .build();

        userApplication.updateInfo(UUID.fromString(jwt.getSubject()), updateRequest);

        return ok(success());
    }

    @Operation(
            summary = "회원탈퇴",
            description = """
                로그인한 회원의 계정을 삭제합니다.
                Keycloak에서 삭제되고, DB에서는 논리 삭제 됩니다.
            """
    )
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> unsubscribe(@AuthenticationPrincipal Jwt jwt) {
        userApplication.delete(UUID.fromString(jwt.getSubject()));

        return ok(success());
    }
}
