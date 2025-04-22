package com.cagan.walletapi.data.repository;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN FETCH t.wallet WHERE t.wallet.id = :walletId ORDER BY t.createdAt DESC")
    List<Transaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Transaction t JOIN FETCH t.wallet WHERE t.id = :transactionId")
    Optional<Transaction> findByIdWithWallet(Long transactionId);

    @Transactional
    @Modifying
    @Query("UPDATE Transaction t SET t.status = :status WHERE t.id = :transactionId")
    void updateTransactionStatus(Long transactionId, TransactionStatusType status);
}
