package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.AmountFilterType;
import com.cagan.walletapi.util.enums.CurrencyType;

import java.math.BigDecimal;

public record SearchWalletDto(Long customerId, CurrencyType currency, BigDecimal amount, AmountFilterType amountFilterType) {
}