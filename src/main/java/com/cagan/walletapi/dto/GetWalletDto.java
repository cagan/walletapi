package com.cagan.walletapi.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record GetWalletDto(Long id, String walletName, String currency, boolean activeForShopping, boolean activeForWithdraw, BigDecimal balance, BigDecimal usableBalance, Long customerId) {
}
