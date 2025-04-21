package com.cagan.walletapi.rest.request;

import com.cagan.walletapi.util.enums.AmountFilterType;
import com.cagan.walletapi.util.enums.CurrencyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
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
public class SearchWalletRequest {

    private Long customerId;

    private CurrencyType currency;

    private BigDecimal amount;

    private AmountFilterType amountFilterType;

    @AssertTrue(message = "amountFilterType must be provided when amount is specified")
    private boolean isAmountFilterTypeValid() {
        return amount == null || amountFilterType != null;
    }
}
