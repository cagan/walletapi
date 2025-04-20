package com.cagan.walletapi.dto;

public record CreateWalletDto(String walletName, String currency, boolean activeForShopping,
                              boolean activeForWithdraw) {
}
