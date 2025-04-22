package com.cagan.walletapi.dto;

import lombok.Builder;

@Builder
public record CreateWalletDto(String walletName, String currency, boolean activeForShopping,
                              boolean activeForWithdraw) {
}
