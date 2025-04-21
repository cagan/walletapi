package com.cagan.walletapi.service.wallet;

import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.data.repository.WalletRepository;
import com.cagan.walletapi.data.spec.SearchWalletSpec;
import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.SearchWalletDto;
import com.cagan.walletapi.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletValidationService walletValidationService;
    private final WalletRepository walletRepository;
    private final SearchWalletSpec searchWalletSpec;
    private final WalletMapper walletMapper = WalletMapper.INSTANCE;

    public GetWalletDto createWallet(CreateWalletDto createWalletDto) {
        walletValidationService.validateCreation(createWalletDto);

        Wallet wallet = walletMapper.toEntity(createWalletDto);
        Wallet savedWallet = walletRepository.save(wallet);

        log.info("Wallet has been created: {}", savedWallet);
        return walletMapper.toGetWalletDto(savedWallet);
    }

    public List<GetWalletDto> searchWallets(SearchWalletDto searchWalletDto) {
        return searchWalletSpec.searchWallets(searchWalletDto);
    }

    @Transactional(readOnly = true)
    public Optional<GetWalletDto> getWalletById(Long id) {
        return walletRepository.findByIdWithLock(id)
                .map(walletMapper::toGetWalletDto);
    }

    public void updateBalanceAndUsableBalance(Long walletId, BigDecimal balance, BigDecimal usableBalance) {
        walletRepository.updateBalanceAndUsableBalance(walletId, balance, usableBalance);
    }

    public void updateBalance(Long walletId, BigDecimal balance) {
        walletRepository.updateBalance(walletId, balance);
    }
}