package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateTransactionDto(Long walletId, BigDecimal amount, TransactionType type,
                                   OppositePartyType oppositePartyType, String source, String destination) {
}
