package com.sparta.deliveryi.store.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreApi {

    private final StoreRegister storeRegister;

    @PostMapping("/v1/stores")
    public ResponseEntity<ApiResponse<StoreRegisterResponse>> register(@RequestBody @Valid StoreRegisterRequest registerRequest) {
        Store store = storeRegister.register(registerRequest);

        return ResponseEntity.ok(ApiResponse.successWithDataOnly(StoreRegisterResponse.from(store)));
    }

}
