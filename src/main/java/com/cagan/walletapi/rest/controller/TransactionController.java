package com.cagan.walletapi.rest.controller;

import com.cagan.walletapi.dto.ApprovalTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.mapper.TransactionMapper;
import com.cagan.walletapi.rest.request.ApprovalTransactionRequest;
import com.cagan.walletapi.service.transaction.TransactionApprovalService;
import com.cagan.walletapi.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<GetTransactionDto>> getTransactionsByWalletId(@PathVariable Long walletId) {
        List<GetTransactionDto> transactionDtoList = transactionService.getTransactionsByWalletId(walletId);
        return ResponseEntity.ok(transactionDtoList);
    }

    @PostMapping("/approval")
    public ResponseEntity<Void> approveTransaction(@Valid @RequestBody ApprovalTransactionRequest request) {
        ApprovalTransactionDto approvalTransactionDto = transactionMapper.toApprovalTransactionDto(request);
        transactionApprovalService.manageApproval(approvalTransactionDto);
        return ResponseEntity.noContent().build();
    }

}
