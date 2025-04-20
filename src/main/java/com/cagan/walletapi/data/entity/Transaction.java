package com.cagan.walletapi.data.entity;

import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.TransactionType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "wallet_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @Column(nullable = false, columnDefinition = "decimal(10, 2)", scale = 2, precision = 5)
    private BigDecimal amount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, columnDefinition = "varchar(10)")
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, columnDefinition = "varchar(10)")
    private OppositePartyType oppositePartyType;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
