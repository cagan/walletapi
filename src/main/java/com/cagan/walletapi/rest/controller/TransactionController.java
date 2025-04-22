package com.cagan.walletapi.rest.controller;

import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{walletId}")
    public ResponseEntity<List<GetTransactionDto>> getTransactionsByWalletId(@PathVariable Long walletId) {
        List<GetTransactionDto> transactionDtoList = transactionService.getTransactionsByWalletId(walletId);
        return ResponseEntity.ok(transactionDtoList);
    }

    @PostMapping("/approval/{transactionId}")
    public ResponseEntity<Void> approveTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.noContent().build();
    }

}
