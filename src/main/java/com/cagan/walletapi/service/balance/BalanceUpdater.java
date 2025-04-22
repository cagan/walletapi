package com.cagan.walletapi.service.balance;


import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import com.cagan.walletapi.util.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceUpdater {
    private final WalletService walletService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateBalance(TransactionStatusType transactionStatus, GetWalletDto wallet, BigDecimal amount, TransactionType transactionType) {
        BigDecimal newBalance = ObjectUtils.defaultIfNull(wallet.balance(), BigDecimal.ZERO);
        BigDecimal newUsableBalance = ObjectUtils.defaultIfNull(wallet.usableBalance(), BigDecimal.ZERO);

        if (TransactionType.DEPOSIT.equals(transactionType)) {
            newBalance = newBalance.add(amount);
            newUsableBalance = newUsableBalance.add(amount);
        } else if (TransactionType.WITHDRAW.equals(transactionType)) {
            newBalance = newBalance.subtract(amount);
            newUsableBalance = newUsableBalance.subtract(amount);
        }

        if (TransactionStatusType.APPROVED.equals(transactionStatus)) {
            walletService.updateBalanceAndUsableBalance(wallet.id(), newBalance, newUsableBalance);
            log.info("Wallet {} balance {} and usable balance {} updated", wallet.id(), newBalance, newUsableBalance);
        } else if (TransactionStatusType.PENDING.equals(transactionStatus)) {
            if (TransactionType.WITHDRAW.equals(transactionType)) {
                walletService.updateUsableBalance(wallet.id(), newUsableBalance);
                log.info("Wallet {} usable balance {} updated", wallet.id(), newUsableBalance);
            } else if (TransactionType.DEPOSIT.equals(transactionType)) {
                walletService.updateBalance(wallet.id(), newBalance);
                log.info("Wallet {} balance {} updated", wallet.id(), newBalance);
            }
        }
    }
}