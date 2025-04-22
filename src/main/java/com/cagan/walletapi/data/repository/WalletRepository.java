package com.cagan.walletapi.data.repository;

import com.cagan.walletapi.data.entity.Wallet;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :walletId")
    @QueryHints(value = @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
    Optional<Wallet> findByIdWithLock(Long walletId);

    @Modifying
    @Transactional
    @Query("UPDATE Wallet w SET w.balance = :balance, w.usableBalance = :usableBalance WHERE w.id = :walletId")
    void updateBalanceAndUsableBalance(Long walletId, BigDecimal balance, BigDecimal usableBalance);


    @Modifying
    @Transactional
    @Query("UPDATE Wallet w SET w.balance = :balance WHERE w.id = :walletId")
    void updateBalance(Long walletId, BigDecimal balance);

    @Modifying
    @Transactional
    @Query("UPDATE Wallet w SET w.usableBalance = :usableBalance WHERE w.id = :walletId")
    void updateUsableBalance(Long walletId, Object o, BigDecimal usableBalance);
}
