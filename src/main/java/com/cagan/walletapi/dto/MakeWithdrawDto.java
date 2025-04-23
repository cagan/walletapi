package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.Role;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record MakeWithdrawDto(BigDecimal amount, Long walletId, String destination,
                              OppositePartyType oppositePartyType, Long customerId, Role role) {
}
