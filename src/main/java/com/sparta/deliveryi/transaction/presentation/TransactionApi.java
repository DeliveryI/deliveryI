package com.sparta.deliveryi.transaction.presentation;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.transaction.application.dto.TransactionSearchRequest;
import com.sparta.deliveryi.transaction.application.service.TransactionApplication;
import com.sparta.deliveryi.transaction.domain.Transaction;
import com.sparta.deliveryi.transaction.presentation.dto.TransactionInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments/transactions")
@Tag(name = "결제 트랜잭션 API", description = "결제 관련 트랜잭션(승인, 취소, 실패 등) 내역 조회 기능 제공")
public class TransactionApi {

    private final TransactionApplication transactionApplication;

    @Operation(
            summary = "결제 트랜잭션 목록 조회(관리자용)",
            description = """
                    전체 결제 트랜잭션 목록을 검색 및 페이징 조회합니다.
                    검색 조건
                        - 주문 ID (orderId)
                        - 거래 유형 (type: REQUEST/APPROVE/REFUND)
                        - 거래 상태 (status: SUCCESS/FAIL/PENDING)
                        - 사용자 아이디 (username)
                    MANAGER/MASTER만 접근 가능합니다.
            """
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TransactionInfoResponse>>> getTransactions (
            @AuthenticationPrincipal Jwt jwt,
            @ParameterObject TransactionSearchRequest search,
            @ParameterObject Pageable pageable
    ) {
        Page<Transaction> transactions = transactionApplication.search(UUID.fromString(jwt.getSubject()), search, pageable);
        Page<TransactionInfoResponse> response = transactions.map(TransactionInfoResponse::from);
        return ok(successWithDataOnly(response));
    }
}
