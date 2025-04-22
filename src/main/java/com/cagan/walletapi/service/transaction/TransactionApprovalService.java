package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.dto.ApprovalTransactionDto;
import com.cagan.walletapi.dto.GetTransactionWithWalletDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.mapper.TransactionMapper;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionApprovalService {
    private final TransactionService transactionService;
    private final TransactionApprovalFactory transactionApprovalFactory;
    private final TransactionMapper transactionMapper = TransactionMapper.INSTANCE;
    private final WalletService walletService;

    @Transactional
    public void manageApproval(ApprovalTransactionDto approvalTransactionDto) {
        GetTransactionWithWalletDto transactionDto = transactionService.getTransactionByIdWithWallet(approvalTransactionDto.transactionId())
                .orElseThrow(() -> new BusinessException("Transaction not found for given ID: " + approvalTransactionDto.transactionId()));

        checkTransactionStatus(transactionDto);

        Transaction transaction = transactionMapper.toEntity(transactionDto);
        Wallet wallet = transaction.getWallet();
        TransactionApprovalStrategy approvalStrategy = transactionApprovalFactory.getStrategy(approvalTransactionDto.status());
        approvalStrategy.handle(transaction, wallet);

        transactionService.updateTransactionStatus(transaction.getId(), approvalTransactionDto.status());
        walletService.upsertWallet(wallet);

        log.info("Transaction {} approval process has been done: {}", transaction.getId(), approvalTransactionDto);
    }

    private void checkTransactionStatus(GetTransactionWithWalletDto transaction) {
        if (TransactionStatusType.APPROVED.equals(transaction.status()) || TransactionStatusType.DENIED.equals(transaction.status())) {
            throw new BusinessException("Transaction is already in the desired state: " + transaction.status());
        }
    }
}
