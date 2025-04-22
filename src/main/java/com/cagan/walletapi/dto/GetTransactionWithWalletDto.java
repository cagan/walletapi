package com.cagan.walletapi.dto;


import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import com.cagan.walletapi.util.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record GetTransactionWithWalletDto(Long id, Wallet wallet, BigDecimal amount, TransactionType type,
                                          OppositePartyType oppositePartyType, TransactionStatusType status,
                                          LocalDateTime createdAt,
                                          LocalDateTime updatedAt) {
}
