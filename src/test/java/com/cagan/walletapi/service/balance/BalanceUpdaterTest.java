package com.cagan.walletapi.service.balance;

import com.cagan.walletapi.dto.GetWalletDto;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BalanceUpdaterTest {

    @Mock
    WalletService walletService;

    @InjectMocks
    BalanceUpdater balanceUpdater;

    @Test
    @DisplayName("updateBalance should add amount to wallet balance when deposit selected")
    void updateBalance_should_add_amount_to_wallet_balance_when_deposit_selected() {
        GetWalletDto walletDto = GetWalletDto.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(100))
                .usableBalance(BigDecimal.valueOf(50))
                .build();

        BigDecimal depositAmount = BigDecimal.valueOf(50);

        balanceUpdater.updateBalance(TransactionStatusType.PENDING, walletDto, depositAmount, TransactionType.DEPOSIT);

        BigDecimal expectedBalance = walletDto.balance().add(depositAmount);

        verify(walletService, times(1)).updateBalance(walletDto.id(), expectedBalance);
    }

    @Test
    @DisplayName("updateBalance should update balance and usable balance when transaction is approved")
    void updateBalance_should_update_balance_and_usable_balance_when_transaction_is_approved() {
        GetWalletDto walletDto = GetWalletDto.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(100))
                .usableBalance(BigDecimal.valueOf(50))
                .build();

        BigDecimal depositAmount = BigDecimal.valueOf(10);

        balanceUpdater.updateBalance(TransactionStatusType.APPROVED, walletDto, depositAmount, TransactionType.WITHDRAW);

        BigDecimal expectedBalance = walletDto.balance().subtract(depositAmount);
        BigDecimal expectedUsableBalance = walletDto.usableBalance().subtract(depositAmount);

        verify(walletService, times(1)).updateBalanceAndUsableBalance(walletDto.id(), expectedBalance, expectedUsableBalance);
    }

    @Test
    @DisplayName("updateBalance should update usable balance only when transaction is pending and transaction type is withdraw")
    void updateBalance_should_update_usable_balance_only_when_transaction_type_is_withdraw() {
        GetWalletDto walletDto = GetWalletDto.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(100))
                .usableBalance(BigDecimal.valueOf(50))
                .build();

        BigDecimal depositAmount = BigDecimal.valueOf(10);

        balanceUpdater.updateBalance(TransactionStatusType.PENDING, walletDto, depositAmount, TransactionType.WITHDRAW);

        BigDecimal expectedUsableBalance= walletDto.usableBalance().subtract(depositAmount);

        verify(walletService, times(1)).updateUsableBalance(walletDto.id(), expectedUsableBalance);
    }

    @Test
    @DisplayName("updateBalance should update balance only when transaction is pending and transaction type is deposit")
    void updateBalance_should_update_usable_balance_only_when_transaction_type_is_deposit() {
        GetWalletDto walletDto = GetWalletDto.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(100))
                .usableBalance(BigDecimal.valueOf(50))
                .build();

        BigDecimal depositAmount = BigDecimal.valueOf(10);

        balanceUpdater.updateBalance(TransactionStatusType.PENDING, walletDto, depositAmount, TransactionType.DEPOSIT);

        BigDecimal expectedBalance= walletDto.balance().add(depositAmount);

        verify(walletService, times(1)).updateBalance(walletDto.id(), expectedBalance);
    }

}