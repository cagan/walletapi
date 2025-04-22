package com.cagan.walletapi.service.withdraw;

import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.MakeWithdrawDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.mapper.WithdrawMapper;
import com.cagan.walletapi.service.balance.BalanceUpdater;
import com.cagan.walletapi.service.transaction.TransactionService;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final BalanceUpdater balanceUpdater;
    private final WithdrawMapper withdrawMapper = WithdrawMapper.INSTANCE;

    @Transactional
    public void makeWithdraw(MakeWithdrawDto makeWithdrawDto) {
        GetWalletDto wallet = walletService.getWalletById(makeWithdrawDto.walletId())
                .orElseThrow(() -> new BusinessException("Wallet not found for given ID: " + makeWithdrawDto.walletId()));

        checkWalletSettingsForWithdraw(wallet);
        checkSufficientBalance(wallet, makeWithdrawDto.amount());

        CreateTransactionDto createTransactionDto = withdrawMapper.toCreateTransactionDto(makeWithdrawDto);
        GetTransactionDto transaction = transactionService.createTransaction(createTransactionDto);

        balanceUpdater.updateBalance(transaction.status(), wallet, makeWithdrawDto.amount(), TransactionType.WITHDRAW);
        log.info("Withdrawal has been made: {}", makeWithdrawDto);
    }

    private void checkWalletSettingsForWithdraw(GetWalletDto wallet) {
        if (!wallet.activeForWithdraw()) {
            log.error("Withdrawal for wallet {} are not active", wallet.id());
            throw new BusinessException("Withdrawal for this wallet are not active");
        }

        if (!wallet.activeForShopping()) {
            log.error("Shopping for wallet {} are not active", wallet.id());
            throw new BusinessException("Shopping for this wallet are not active");
        }
    }

    private void checkSufficientBalance(GetWalletDto wallet, BigDecimal amount) {
        if (ObjectUtils.defaultIfNull(wallet.usableBalance(), BigDecimal.ZERO).compareTo(amount) < 0) {
            log.error("Insufficient balance for wallet {} to make withdrawal", wallet.id());
            throw new BusinessException("Insufficient balance for this wallet to make withdrawal");
        }
    }
}
