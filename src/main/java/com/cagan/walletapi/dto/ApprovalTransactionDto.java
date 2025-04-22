package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.TransactionStatusType;
import lombok.Builder;

@Builder
public record ApprovalTransactionDto(Long transactionId, TransactionStatusType status) {
}
