package com.cagan.walletapi.service.deposit;

import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.MakeDepositDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.service.balance.BalanceUpdater;
import com.cagan.walletapi.service.transaction.TransactionService;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.*;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @Mock
    WalletService walletService;

    @Mock
    TransactionService transactionService;

    @Mock
    BalanceUpdater balanceUpdater;

    @InjectMocks
    DepositService depositService;

    @Test
    @DisplayName("makeDeposit should throw exception when wallet not found")
    void makeDeposit_should_throw_exception_when_wallet_not_found() {
        when(walletService.getWalletById(1L))
                .thenReturn(Optional.empty());

        MakeDepositDto makeDepositDto = new MakeDepositDto(BigDecimal.valueOf(100), 1L, "1234-1234-1234-1234", OppositePartyType.IBAN, 1L, Role.EMPLOYEE);

        assertThatThrownBy(() -> depositService.makeDeposit(makeDepositDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Wallet not found for given ID: " + 1);

        verify(walletService).getWalletById(1L);
    }

    @ParameterizedTest
    @DisplayName("makeDeposit should throw exception when deposit amount is not proper")
    @ValueSource(longs = {0, -100})
    void makeDeposit_should_throw_exception_when_deposit_amount_is_not_proper(Long value) {
        MakeDepositDto makeDepositDto = new MakeDepositDto(BigDecimal.valueOf(value), 1L, "1234-1234-1234-1234", OppositePartyType.IBAN, 1L, Role.EMPLOYEE);

        GetWalletDto walletDto = new GetWalletDto(1L, "Test Wallet", CurrencyType.EUR.toString(), false, true, BigDecimal.ZERO, BigDecimal.ZERO, 1L);

        when(walletService.getWalletById(1L))
                .thenReturn(Optional.of(walletDto));

        assertThatThrownBy(() -> depositService.makeDeposit(makeDepositDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Deposit amount must be greater than zero" + makeDepositDto.amount());

        verify(walletService).getWalletById(1L);
    }

    @Test
    @DisplayName("makeDeposit should make deposit by invoking transaction and balance update logics properly")
    void makeDeposit_should_make_withdraw_by_invoking_transaction_and_balance_update_logics_properly() {
        MakeDepositDto makeDepositDto = new MakeDepositDto(BigDecimal.valueOf(100), 1L, "1234-1234-1234-1234", OppositePartyType.IBAN, 1L, Role.EMPLOYEE);

        GetWalletDto walletDto = new GetWalletDto(1L, "Test Wallet", CurrencyType.EUR.toString(), false, true, BigDecimal.ZERO, BigDecimal.ZERO, 1L);

        when(walletService.getWalletById(1L))
                .thenReturn(Optional.of(walletDto));

        CreateTransactionDto createTransactionDto = CreateTransactionDto.builder()
                .type(TransactionType.DEPOSIT)
                .amount(makeDepositDto.amount())
                .oppositePartyType(makeDepositDto.oppositePartyType())
                .source(makeDepositDto.source())
                .walletId(makeDepositDto.walletId())
                .build();

        GetTransactionDto getTransactionDto = GetTransactionDto.builder()
                .id(1L)
                .walletId(makeDepositDto.walletId())
                .status(TransactionStatusType.APPROVED)
                .build();

        when(transactionService.createTransaction(createTransactionDto)).thenReturn(getTransactionDto);

        depositService.makeDeposit(makeDepositDto);

        verify(transactionService, times(1)).createTransaction(createTransactionDto);
        verify(balanceUpdater, times(1)).updateBalance(getTransactionDto.status(), walletDto, makeDepositDto.amount(), TransactionType.DEPOSIT);
    }
}