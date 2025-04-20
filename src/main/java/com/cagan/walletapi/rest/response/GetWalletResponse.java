package com.cagan.walletapi.rest.response;

import java.math.BigDecimal;

public record GetWalletResponse(Long id, String walletName, String currency, boolean activeForShopping,
                                boolean activeForWithdraw, BigDecimal balance, BigDecimal usableBalance) {
}
