package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.util.enums.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class DenyStrategyTest {

    @Test
    @DisplayName("handleDeposit")
    void handleDeposit() {
        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(50))
                .build();

        Wallet wallet =Wallet.builder()
                .balance(BigDecimal.valueOf(100))
                .build();

        BigDecimal expectedBalance = wallet.getBalance().subtract(transaction.getAmount());

        TransactionApprovalStrategy transactionApprovalStrategy = new DenyStrategy();
        transactionApprovalStrategy.handle(transaction, wallet);

        assertThat(wallet.getBalance()).isEqualTo(expectedBalance);
    }

    @Test
    @DisplayName("handleWithdraw")
    void handleWithdraw() {
        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(50))
                .build();

        Wallet wallet =Wallet.builder()
                .usableBalance(BigDecimal.valueOf(100))
                .build();

        BigDecimal expectedUsableBalance = wallet.getUsableBalance().add(transaction.getAmount());

        TransactionApprovalStrategy transactionApprovalStrategy = new DenyStrategy();
        transactionApprovalStrategy.handle(transaction, wallet);

        assertThat(wallet.getUsableBalance()).isEqualTo(expectedUsableBalance);
    }


}