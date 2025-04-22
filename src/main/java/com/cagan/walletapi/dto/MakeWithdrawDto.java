package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.OppositePartyType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MakeWithdrawDto(BigDecimal amount, Long walletId, String destination,
                              OppositePartyType oppositePartyType) {
}
