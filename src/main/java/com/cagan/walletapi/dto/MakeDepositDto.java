package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.OppositePartyType;

import java.math.BigDecimal;

public record MakeDepositDto(BigDecimal amount, Long walletId, String source, OppositePartyType oppositePartyType) {
}
