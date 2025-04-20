package com.cagan.walletapi.service;

import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.data.repository.WalletRepository;
import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.mapper.WalletMapper;
import com.cagan.walletapi.util.enums.CurrencyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper = WalletMapper.INSTANCE;

    public GetWalletDto createWallet(CreateWalletDto createWalletDto) {
        validateCreation(createWalletDto);

        Wallet wallet = walletMapper.toEntity(createWalletDto);
        Wallet savedWallet = walletRepository.save(wallet);
        log.info("Wallet has been created: {}", savedWallet);
        return walletMapper.toGetWalletDto(savedWallet);
    }

    public void validateCreation(CreateWalletDto createWalletDto) {
        boolean nullRequiredValues = Stream.of(CurrencyType.fromValue(createWalletDto.currency()), createWalletDto.walletName())
                .anyMatch(Objects::isNull);

        if (nullRequiredValues) {
            // throw
        }

    }
}
