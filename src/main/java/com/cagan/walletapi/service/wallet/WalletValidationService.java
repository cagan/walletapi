package com.cagan.walletapi.service.wallet;

import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.util.enums.CurrencyType;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class WalletValidationService {

    public void validateCreation(CreateWalletDto createWalletDto) {
        boolean nullRequiredValues = Stream.of(CurrencyType.fromValue(createWalletDto.currency()), createWalletDto.walletName())
                .anyMatch(Objects::isNull);

        if (nullRequiredValues) {
            // throw
        }

    }
}
