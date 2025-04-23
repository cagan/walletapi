package com.cagan.walletapi.dto;

import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.Role;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record MakeDepositDto(BigDecimal amount, Long walletId, String source, OppositePartyType oppositePartyType, Long customerId, Role role) {
}
