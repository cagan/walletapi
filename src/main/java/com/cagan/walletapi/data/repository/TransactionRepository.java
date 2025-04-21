package com.cagan.walletapi.data.repository;

import com.cagan.walletapi.data.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
