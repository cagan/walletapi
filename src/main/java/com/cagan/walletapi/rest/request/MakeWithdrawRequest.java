package com.cagan.walletapi.rest.request;

import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.validator.EnumValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MakeWithdrawRequest {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long walletId;

    @NotNull
    private String destination;

    @NotNull
    @EnumValidator(enumClass = OppositePartyType.class, message = "Must be a valid opposite party type" )
    private OppositePartyType oppositePartyType;
}
