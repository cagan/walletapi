package com.cagan.walletapi.rest.request;

import com.cagan.walletapi.util.enums.TransactionStatusType;
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
public class ApprovalTransactionRequest {

    @NotNull
    private Long transactionId;

    @NotNull
    @EnumValidator(enumClass = TransactionStatusType.class, message = "Must be a valid transaction status type")
    private TransactionStatusType status;
}
