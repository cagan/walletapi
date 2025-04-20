package com.cagan.walletapi.rest.request;

import com.cagan.walletapi.util.enums.CurrencyType;
import com.cagan.walletapi.validator.EnumValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateWalletRequest {

    @NotNull
    String walletName;

    @EnumValidator(enumClass = CurrencyType.class, message = "Must be a valid currency type")
    CurrencyType currency;

    boolean activeForShopping = true;

    boolean activeForWithdraw = true;
}