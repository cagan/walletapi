package com.cagan.walletapi.service.wallet;

import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.data.repository.WalletRepository;
import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.util.enums.CurrencyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    WalletRepository walletRepository;

    @InjectMocks
    WalletService walletService;

    @Captor
    ArgumentCaptor<Wallet> walletArgumentCaptor;


    @Test
    @DisplayName("createWallet should throw exception when missing required values")
    void createWallet_should_throw_exception_when_missing_required_values() {
        CreateWalletDto createWalletDto = CreateWalletDto.builder()
                .build();
        assertThatThrownBy(() -> walletService.createWallet(createWalletDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Missing required values for creating a wallet");

        verifyNoInteractions(walletRepository);
    }

    @Test
    @DisplayName("createWallet should create wallet properly")
    void createWallet_should_create_wallet_properly() {
        CreateWalletDto createWalletDto = CreateWalletDto.builder()
                .walletName("test")
                .activeForShopping(true)
                .activeForShopping(false)
                .currency(CurrencyType.USD.toString())
                .build();

        walletService.createWallet(createWalletDto);

        verify(walletRepository, times(1)).save(walletArgumentCaptor.capture());

        assertThat(walletArgumentCaptor.getValue().getWalletName()).isEqualTo(createWalletDto.walletName());
        assertThat(walletArgumentCaptor.getValue().getActiveForShopping()).isEqualTo(createWalletDto.activeForShopping());
        assertThat(walletArgumentCaptor.getValue().getActiveForWithdraw()).isEqualTo(createWalletDto.activeForWithdraw());
        assertThat(walletArgumentCaptor.getValue().getCurrency().toString()).isEqualTo(createWalletDto.currency());
    }
}