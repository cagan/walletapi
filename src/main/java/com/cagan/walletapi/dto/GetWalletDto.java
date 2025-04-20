package com.cagan.walletapi.dto;

import java.math.BigDecimal;

public record GetWalletDto(Long id, String walletName, String currency, boolean activeForShopping, boolean activeForWithdraw, BigDecimal balance, BigDecimal usableBalance) {
}
