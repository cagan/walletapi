package com.cagan.walletapi.data.repository;

import com.cagan.walletapi.data.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN FETCH t.wallet WHERE t.wallet.id = :walletId ORDER BY t.createdAt DESC")
    List<Transaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);
}
