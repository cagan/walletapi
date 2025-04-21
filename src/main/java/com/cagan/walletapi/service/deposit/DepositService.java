package com.cagan.walletapi.service.deposit;

import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.MakeDepositDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.mapper.DepositMapper;
import com.cagan.walletapi.service.transaction.TransactionService;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final DepositMapper depositMapper = DepositMapper.INSTANCE;

    @Transactional
    public void makeDeposit(MakeDepositDto makeDepositDto) {
        GetWalletDto walletDto = walletService.getWalletById(makeDepositDto.walletId())
                .orElseThrow(() -> new BusinessException("Wallet not found for given ID: " + makeDepositDto.walletId()));

        if (makeDepositDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Deposit amount must be greater than zero" + makeDepositDto.amount());
        }

        // TODO users should only deposit their own wallets
        CreateTransactionDto createTransactionDto = depositMapper.toCreateTransactionDto(makeDepositDto);
        log.info("Deposit has been made: {}", makeDepositDto);
        GetTransactionDto transaction = transactionService.createTransaction(createTransactionDto);

        updateBalance(transaction, walletDto, makeDepositDto);
    }

    private void updateBalance(GetTransactionDto transaction, GetWalletDto walletDto, MakeDepositDto makeDepositDto) {
        BigDecimal newBalance = ObjectUtils.defaultIfNull(walletDto.balance(), BigDecimal.ZERO).add(makeDepositDto.amount());
        BigDecimal newUsableBalance = ObjectUtils.defaultIfNull(walletDto.usableBalance(), BigDecimal.ZERO).add(makeDepositDto.amount());

        if (TransactionStatusType.APPROVED.equals(transaction.status())) {
            walletService.updateBalanceAndUsableBalance(walletDto.id(), newBalance, newUsableBalance);
            log.info("Wallet {} balance {} and usable balance {} updated", walletDto.id(), newBalance, newUsableBalance);
        } else if (TransactionStatusType.PENDING.equals(transaction.status())) {
            walletService.updateBalance(walletDto.id(), newBalance);
            log.info("Wallet {} balance {} updated", walletDto.id(), newBalance);
        }
    }
}
