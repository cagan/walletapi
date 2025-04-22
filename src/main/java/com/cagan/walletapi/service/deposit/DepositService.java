package com.cagan.walletapi.service.deposit;

import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.MakeDepositDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.mapper.DepositMapper;
import com.cagan.walletapi.service.balance.BalanceUpdater;
import com.cagan.walletapi.service.transaction.TransactionService;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final BalanceUpdater balanceUpdater;
    private final DepositMapper depositMapper = DepositMapper.INSTANCE;

    @Transactional
    public void makeDeposit(MakeDepositDto makeDepositDto) {
        GetWalletDto wallet = walletService.getWalletById(makeDepositDto.walletId())
                .orElseThrow(() -> new BusinessException("Wallet not found for given ID: " + makeDepositDto.walletId()));

        checkDepositAmountEligibility(makeDepositDto);

        // TODO users should only deposit their own wallets
        CreateTransactionDto createTransactionDto = depositMapper.toCreateTransactionDto(makeDepositDto);
        GetTransactionDto transaction = transactionService.createTransaction(createTransactionDto);

        balanceUpdater.updateBalance(transaction.status(), wallet, makeDepositDto.amount(), TransactionType.DEPOSIT);
        log.info("Deposit has been made: {}", makeDepositDto);
    }

    private static void checkDepositAmountEligibility(MakeDepositDto makeDepositDto) {
        if (makeDepositDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Deposit amount must be greater than zero" + makeDepositDto.amount());
        }
    }
}
