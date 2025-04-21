package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.TransactionType;

import java.math.BigDecimal;

public record CreateTransactionDto(Long walletId, BigDecimal amount, TransactionType type,
                                   OppositePartyType oppositePartyType) {
}
