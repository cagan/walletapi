package com.cagan.walletapi.rest.controller;

import com.cagan.walletapi.dto.ApprovalTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.mapper.TransactionMapper;
import com.cagan.walletapi.rest.request.ApprovalTransactionRequest;
import com.cagan.walletapi.security.CustomUserDetails;
import com.cagan.walletapi.service.transaction.TransactionApprovalService;
import com.cagan.walletapi.service.transaction.TransactionService;
import com.cagan.walletapi.util.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final TransactionApprovalService transactionApprovalService;

    @GetMapping("/{walletId}")
    public ResponseEntity<List<GetTransactionDto>> getTransactionsByWalletId(@PathVariable Long walletId, @AuthenticationPrincipal CustomUserDetails user) {
        List<GetTransactionDto> transactionDtoList;

        if (Role.CUSTOMER.equals(user.getRole())) {
            transactionDtoList = transactionService.getTransactionsByWalletIdAndCustomerId(walletId, user.getUserId());
        } else {
            transactionDtoList = transactionService.getTransactionsByWalletId(walletId);
        }

        return ResponseEntity.ok(transactionDtoList);
    }

    @PostMapping("/approval")
    public ResponseEntity<Void> approveTransaction(@Valid @RequestBody ApprovalTransactionRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        ApprovalTransactionDto approvalTransactionDto = transactionMapper.toApprovalTransactionDto(request);
        transactionApprovalService.manageApproval(approvalTransactionDto);
        return ResponseEntity.noContent().build();
    }

}
