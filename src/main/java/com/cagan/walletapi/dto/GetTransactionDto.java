package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import com.cagan.walletapi.util.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetTransactionDto(Long id, Long walletId, BigDecimal amount, TransactionType type,
                                OppositePartyType oppositeParty, TransactionStatusType status, LocalDateTime createdAt,
                                LocalDateTime updatedAt) {
}
