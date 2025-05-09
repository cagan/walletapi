package com.cagan.walletapi.service.wallet;

import com.cagan.walletapi.data.entity.Customer;
import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.data.repository.WalletRepository;
import com.cagan.walletapi.data.spec.SearchWalletSpec;
import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.SearchWalletDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.mapper.WalletMapper;
import com.cagan.walletapi.security.CustomUserDetails;
import com.cagan.walletapi.service.customer.CustomerService;
import com.cagan.walletapi.util.enums.CurrencyType;
import com.cagan.walletapi.util.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final SearchWalletSpec searchWalletSpec;
    private final CustomerService customerService;
    private final WalletMapper walletMapper = WalletMapper.INSTANCE;

    public GetWalletDto createWallet(CreateWalletDto createWalletDto) {
        validateCreation(createWalletDto);

        Wallet wallet = walletMapper.toEntity(createWalletDto);
        setCustomerOfWallet(createWalletDto, wallet);
        Wallet savedWallet = walletRepository.save(wallet);

        log.info("Wallet has been created: {}", savedWallet);
        return walletMapper.toGetWalletDto(savedWallet);
    }

    private void setCustomerOfWallet(CreateWalletDto createWalletDto, Wallet wallet) {
        Customer customer = customerService.getCustomerById(createWalletDto.userId());
        wallet.setCustomer(customer);
    }

    public List<GetWalletDto> searchWallets(SearchWalletDto searchWalletDto, CustomUserDetails user) {
        Role userRole = user.getRole();

        if (userRole.equals(Role.CUSTOMER)) {
            searchWalletDto = searchWalletDto.toBuilder()
                    .customerId(user.getUserId())
                    .build();
        }

        return searchWalletSpec.searchWallets(searchWalletDto);
    }

    private void validateCreation(CreateWalletDto createWalletDto) {
        boolean nullRequiredValues = Stream.of(CurrencyType.fromValue(createWalletDto.currency()), createWalletDto.walletName())
                .anyMatch(Objects::isNull);

        if (nullRequiredValues) {
            throw new BusinessException("Missing required values for creating a wallet");
        }
    }

    @Transactional(readOnly = true)
    public Optional<GetWalletDto> getWalletById(Long id) {
        return walletRepository.findByIdWithLock(id)
                .map(walletMapper::toGetWalletDto);
    }

    @Transactional(readOnly = true)
    public Optional<GetWalletDto> getWalletByIdAndCustomerId(Long id, Long customerId) {
        return walletRepository.findByIdAndCustomerIdWithLock(id, customerId)
                .map(walletMapper::toGetWalletDto);
    }

    public void updateBalanceAndUsableBalance(Long walletId, BigDecimal balance, BigDecimal usableBalance) {
        walletRepository.updateBalanceAndUsableBalance(walletId, balance, usableBalance);
    }

    public void updateBalance(Long walletId, BigDecimal balance) {
        walletRepository.updateBalance(walletId, balance);
    }

    public void updateUsableBalance(Long walletId, BigDecimal usableBalance) {
        walletRepository.updateUsableBalance(walletId, null, usableBalance);
    }

    public void upsertWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }
}