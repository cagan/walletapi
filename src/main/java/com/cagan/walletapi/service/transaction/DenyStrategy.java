package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import com.cagan.walletapi.util.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class DenyStrategy implements TransactionApprovalStrategy {

    @Override
    public void handle(Transaction transaction, Wallet wallet) {
        if (TransactionType.DEPOSIT.equals(transaction.getType())) {
            handleDeposit(transaction, wallet);
        } else if (TransactionType.WITHDRAW.equals(transaction.getType())) {
            handleWithdraw(transaction, wallet);
        }
    }

    private void handleDeposit(Transaction transaction, Wallet wallet) {
        BigDecimal updatedBalance = wallet.getBalance().subtract(transaction.getAmount());
        wallet.setBalance(updatedBalance);
        log.info("Denied Deposit of {} has been processed for wallet {}", transaction.getAmount(), wallet.getId());
    }

    private void handleWithdraw(Transaction transaction, Wallet wallet) {
        BigDecimal updatedBalance = wallet.getUsableBalance().add(transaction.getAmount());
        wallet.setUsableBalance(updatedBalance);
        log.info("Denied Withdrawal of {} has been processed for wallet {}", transaction.getAmount(), wallet.getId());
    }

    @Override
    public TransactionStatusType getTransactionStatusType() {
        return TransactionStatusType.DENIED;
    }
}
