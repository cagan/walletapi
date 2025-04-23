package com.cagan.walletapi.data.entity;

import com.cagan.walletapi.util.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @JoinColumn(name = "customer_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(nullable = false)
    private String walletName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3, columnDefinition = "varchar(3)")
    private CurrencyType currency;

    @Column(nullable = false, columnDefinition = "boolean")
    private Boolean activeForShopping = true;

    @Column(nullable = false, columnDefinition = "boolean")
    private Boolean activeForWithdraw = true;

    @Setter
    @Column(columnDefinition = "decimal(10, 2)", scale = 2, precision = 5)
    private BigDecimal balance;

    @Setter
    @Column(columnDefinition = "decimal(10, 2)", scale = 2, precision = 5)
    private BigDecimal usableBalance;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
