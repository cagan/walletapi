package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.AmountFilterType;
import com.cagan.walletapi.util.enums.CurrencyType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SearchWalletDto(Long walletId, Long customerId, CurrencyType currency, BigDecimal amount, AmountFilterType amountFilterType) {
}