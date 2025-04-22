package com.cagan.walletapi.service.withdraw;

import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.MakeWithdrawDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.service.balance.BalanceUpdater;
import com.cagan.walletapi.service.transaction.TransactionService;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import com.cagan.walletapi.util.enums.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WithdrawServiceTest {
    @Mock
    WalletService walletService;

    @Mock
    TransactionService transactionService;

    @Mock
    BalanceUpdater balanceUpdater;

    @InjectMocks
    WithdrawService withdrawService;

    @Test
    @DisplayName("makeWithdraw should thorow exception when wallet not found")
    void makeWithdraw_should_throw_exception_when_wallet_not_found() {
        long walletId = 1L;
        when(walletService.getWalletById(walletId)).thenReturn(Optional.empty());

        MakeWithdrawDto makeWithdrawDto = MakeWithdrawDto.builder().walletId(walletId).build();
        assertThatThrownBy(() -> withdrawService.makeWithdraw(makeWithdrawDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Wallet not found for given ID: " + walletId);
    }

    @Test
    @DisplayName("makeWithdraw should throw exception when withdraw is not active for withdraw")
    void makeWithdraw_should_throw_exception_when_withdraw_is_not_active() {
        long walletId = 1L;
        GetWalletDto wallet = GetWalletDto.builder()
                .id(walletId)
                .activeForWithdraw(false)
                .build();

        when(walletService.getWalletById(walletId)).thenReturn(Optional.of(wallet));

        MakeWithdrawDto makeWithdrawDto = MakeWithdrawDto.builder().walletId(walletId).build();

        assertThatThrownBy(() -> withdrawService.makeWithdraw(makeWithdrawDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Withdrawal for this wallet are not active");
    }

    @Test
    @DisplayName("makeWithdraw should throw exception when withdraw is not active for shopping")
    void makeWithdraw_should_throw_exception_when_withdraw_is_not_active_for_shopping() {
        long walletId = 1L;
        GetWalletDto wallet = GetWalletDto.builder()
                .id(walletId)
                .activeForWithdraw(true)
                .activeForShopping(false)
                .build();

        when(walletService.getWalletById(walletId)).thenReturn(Optional.of(wallet));

        MakeWithdrawDto makeWithdrawDto = MakeWithdrawDto.builder().walletId(walletId).build();

        assertThatThrownBy(() -> withdrawService.makeWithdraw(makeWithdrawDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Shopping for this wallet are not active");
    }

    @Test
    @DisplayName("makeWithdraw should throw exception when balance is not sufficient")
    void makeWithdraw_should_throw_exception_when_balance_is_not_sufficient() {
        long walletId = 1L;
        GetWalletDto wallet = GetWalletDto.builder()
                .id(walletId)
                .activeForWithdraw(true)
                .activeForShopping(true)
                .balance(BigDecimal.ZERO) // not sufficient balance
                .build();

        when(walletService.getWalletById(walletId)).thenReturn(Optional.of(wallet));

        MakeWithdrawDto makeWithdrawDto = MakeWithdrawDto.builder().walletId(walletId).amount(BigDecimal.valueOf(100)).build();

        assertThatThrownBy(() -> withdrawService.makeWithdraw(makeWithdrawDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Insufficient balance for this wallet to make withdrawal");
    }

    @Test
    @DisplayName("makeWithdraw should make withdraw by invoking transaction and balance update logics properly")
    void makeWithdraw_should_make_withdraw_by_invoking_transaction_and_balance_update_logics_properly() {
        long walletId = 1L;
        GetWalletDto walletDto = GetWalletDto.builder()
                .id(walletId)
                .activeForWithdraw(true)
                .activeForShopping(true)
                .balance(BigDecimal.valueOf(200)) // sufficient balance
                .usableBalance(BigDecimal.valueOf(200))
                .build();

        MakeWithdrawDto makeWithdrawDto = MakeWithdrawDto.builder().walletId(walletId).amount(BigDecimal.valueOf(100)).build();

        CreateTransactionDto createTransactionDto = CreateTransactionDto.builder()
                .type(TransactionType.WITHDRAW)
                .amount(makeWithdrawDto.amount())
                .oppositePartyType(makeWithdrawDto.oppositePartyType())
                .destination(makeWithdrawDto.destination())
                .walletId(makeWithdrawDto.walletId())
                .build();

        GetTransactionDto getTransactionDto = GetTransactionDto.builder()
                .id(1L)
                .walletId(makeWithdrawDto.walletId())
                .status(TransactionStatusType.APPROVED)
                .build();

        when(walletService.getWalletById(walletId)).thenReturn(Optional.of(walletDto));
        when(transactionService.createTransaction(createTransactionDto)).thenReturn(getTransactionDto);

        withdrawService.makeWithdraw(makeWithdrawDto);

        verify(transactionService, times(1)).createTransaction(createTransactionDto);
        verify(balanceUpdater, times(1)).updateBalance(getTransactionDto.status(), walletDto, makeWithdrawDto.amount(), TransactionType.WITHDRAW);
    }
}