package com.cagan.walletapi.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false, length = 25)
    private String surname;

    @Column(nullable = false, length = 20)
    private String tckn;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Wallet> walletList;
}
