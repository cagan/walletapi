package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.data.repository.TransactionRepository;
import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.mapper.TransactionMapper;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper = TransactionMapper.INSTANCE;

    protected static final BigDecimal MAX_TRANSACTION_AMOUNT = BigDecimal.valueOf(1000);

    @Transactional(propagation = Propagation.MANDATORY)
    public GetTransactionDto createTransaction(CreateTransactionDto createTransactionDto) {
        Transaction transaction = transactionMapper.toEntity(createTransactionDto);
        determineTransactionStatus(createTransactionDto, transaction);
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction {} created for wallet: {}", transaction, createTransactionDto.walletId());
        return transactionMapper.toGetTransactionDto(savedTransaction);
    }

    private static void determineTransactionStatus(CreateTransactionDto createTransactionDto, Transaction transaction) {
        TransactionStatusType transactionStatusType = TransactionStatusType.APPROVED;

        if (createTransactionDto.amount().compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
            transactionStatusType = TransactionStatusType.PENDING;
        }

        transaction.setStatus(transactionStatusType);
    }

    public List<GetTransactionDto> getTransactionsByWalletId(Long walletId) {
        List<Transaction> transactionEntities = transactionRepository.findByWalletIdOrderByCreatedAtDesc(walletId);
        return transactionMapper.toGetTransactionDtoList(transactionEntities);
    }
}
