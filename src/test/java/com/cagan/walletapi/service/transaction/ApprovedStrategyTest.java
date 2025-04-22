package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.util.enums.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ApprovedStrategyTest {

    @Test
    @DisplayName("handle should handle deposit transaction")
    void handleDeposit() {
        TransactionApprovalStrategy transactionApprovalStrategy = new ApprovedStrategy();

        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(BigDecimal.valueOf(100))
                .build();

        Wallet wallet = Wallet.builder()
                .usableBalance(BigDecimal.valueOf(50))
                .build();

        BigDecimal expectedUsableBalance = transaction.getAmount().add(wallet.getUsableBalance());
        transactionApprovalStrategy.handle(transaction, wallet);
        assertThat(wallet.getUsableBalance()).isEqualTo(expectedUsableBalance);
    }

    @Test
    @DisplayName("handle should handle withdraw transaction")
    void handleWithdraw() {
        TransactionApprovalStrategy transactionApprovalStrategy = new ApprovedStrategy();

        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(50))
                .build();

        Wallet wallet = Wallet.builder()
                .balance(BigDecimal.valueOf(150))
                .build();

        BigDecimal expectedUsableBalance =  wallet.getBalance().subtract(transaction.getAmount());
        transactionApprovalStrategy.handle(transaction, wallet);
        assertThat(wallet.getBalance()).isEqualTo(expectedUsableBalance);
    }

}