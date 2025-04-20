package com.cagan.walletapi.data.repository;

import com.cagan.walletapi.data.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
