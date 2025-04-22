package com.cagan.walletapi.data.spec;

import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.SearchWalletDto;
import com.cagan.walletapi.mapper.WalletMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SearchWalletSpec {
    private final WalletMapper walletMapper = WalletMapper.INSTANCE;

    @PersistenceContext
    private EntityManager entityManager;


    public List<GetWalletDto> searchWallets(SearchWalletDto searchWalletDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Wallet> query = cb.createQuery(Wallet.class);
        Root<Wallet> wallet = query.from(Wallet.class);

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(searchWalletDto.walletId())) {
            predicates.add(cb.equal(wallet.get("id"), searchWalletDto.walletId()));
        }

        if (Objects.nonNull(searchWalletDto.customerId())) {
            predicates.add(cb.equal(wallet.get("customer").get("id"), searchWalletDto.customerId()));
        }

        if (Objects.nonNull(searchWalletDto.currency())) {
            predicates.add(cb.equal(wallet.get("currency"), searchWalletDto.currency()));
        }

        if (Objects.nonNull(searchWalletDto.amount()) && Objects.nonNull(searchWalletDto.amountFilterType())) {
            switch (searchWalletDto.amountFilterType()) {
                case GT:
                    predicates.add(cb.greaterThan(wallet.get("balance"), searchWalletDto.amount()));
                    break;
                case LT:
                    predicates.add(cb.lessThan(wallet.get("balance"), searchWalletDto.amount()));
                    break;
                case EQ:
                    predicates.add(cb.equal(wallet.get("balance"), searchWalletDto.amount()));
                    break;
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        List<Wallet> wallets = entityManager.createQuery(query).getResultList();
        return walletMapper.toGetWalletDtoList(wallets);
    }
}