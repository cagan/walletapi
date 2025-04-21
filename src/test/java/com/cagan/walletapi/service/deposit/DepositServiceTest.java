package com.cagan.walletapi.service.deposit;

import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.MakeDepositDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.service.transaction.TransactionService;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.CurrencyType;
import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {
    @Mock
    WalletService walletService;

    @Mock
    TransactionService transactionService;

    @InjectMocks
    DepositService depositService;


    @Test
    @DisplayName("makeDeposit should throw exception when wallet not found")
    void makeDeposit_should_throw_exception_when_wallet_not_found() {
        when(walletService.getWalletById(1L))
                .thenReturn(Optional.empty());

        MakeDepositDto makeDepositDto = new MakeDepositDto(BigDecimal.valueOf(100), 1L, "1234-1234-1234-1234", OppositePartyType.IBAN);

        AssertionsForInterfaceTypes.assertThatThrownBy(() -> depositService.makeDeposit(makeDepositDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Wallet not found for given ID: " + 1);

        verify(walletService).getWalletById(1L);
    }

    @ParameterizedTest
    @DisplayName("makeDeposit should throw exception when deposit amount is not proper")
    @ValueSource(longs = {0, -100})
    void makeDeposit_should_throw_exception_when_deposit_amount_is_not_proper(Long value) {
        MakeDepositDto makeDepositDto = new MakeDepositDto(BigDecimal.valueOf(value), 1L, "1234-1234-1234-1234", OppositePartyType.IBAN);

        GetWalletDto walletDto = new GetWalletDto(1L, "Test Wallet", CurrencyType.EUR.toString(), false, true, BigDecimal.ZERO, BigDecimal.ZERO);

        when(walletService.getWalletById(1L))
                .thenReturn(Optional.of(walletDto));

        AssertionsForInterfaceTypes.assertThatThrownBy(() -> depositService.makeDeposit(makeDepositDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Deposit amount must be greater than zero" + makeDepositDto.amount());

        verify(walletService).getWalletById(1L);
    }

    @Test
    @DisplayName("makeDeposit should update wallet balance and usable balance when transaction is approved")
    void makeDeposit_should_update_wallet_balance_and_usable_balance_when_transaction_is_approved() {
        MakeDepositDto makeDepositDto = new MakeDepositDto(BigDecimal.valueOf(100), 1L, "1234-1234-1234-1234", OppositePartyType.IBAN);

        GetWalletDto walletDto = new GetWalletDto(1L, "Test Wallet", CurrencyType.EUR.toString(), false, true, BigDecimal.ZERO, BigDecimal.ZERO);

        when(walletService.getWalletById(1L))
                .thenReturn(Optional.of(walletDto));

        GetTransactionDto getTransactionDto = GetTransactionDto.builder()
                .id(1L)
                .walletId(1L)
                .status(TransactionStatusType.APPROVED)
                .build();
        when(transactionService.createTransaction(any())).thenReturn(getTransactionDto);

        depositService.makeDeposit(makeDepositDto);
        verify(walletService).updateBalanceAndUsableBalance(1L, BigDecimal.valueOf(100), BigDecimal.valueOf(100));
        verify(transactionService).createTransaction(any());
    }

    @Test
    @DisplayName("makeDeposit should update wallet balance only when transaction is approved")
    void makeDeposit_should_update_wallet_balance_only_when_transaction_is_approved() {
        MakeDepositDto makeDepositDto = new MakeDepositDto(BigDecimal.valueOf(5000), 1L, "1234-1234-1234-1234", OppositePartyType.IBAN);

        GetWalletDto walletDto = new GetWalletDto(1L, "Test Wallet", CurrencyType.EUR.toString(), false, true, BigDecimal.ZERO, BigDecimal.ZERO);

        when(walletService.getWalletById(1L))
                .thenReturn(Optional.of(walletDto));

        GetTransactionDto getTransactionDto = GetTransactionDto.builder()
                .id(1L)
                .walletId(1L)
                .status(TransactionStatusType.PENDING)
                .build();
        when(transactionService.createTransaction(any())).thenReturn(getTransactionDto);

        depositService.makeDeposit(makeDepositDto);
        verify(walletService).updateBalance(1L, BigDecimal.valueOf(5000));
        verify(transactionService).createTransaction(any());
    }

}