package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.dto.ApprovalTransactionDto;
import com.cagan.walletapi.dto.GetTransactionWithWalletDto;
import com.cagan.walletapi.error.BusinessException;
import com.cagan.walletapi.service.wallet.WalletService;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionApprovalServiceTest {
    @Mock
    TransactionService transactionService;

    @Mock
    TransactionApprovalFactory transactionApprovalFactory;

    @Mock
    WalletService walletService;

    @InjectMocks
    TransactionApprovalService transactionApprovalService;

    @Mock
    TransactionApprovalStrategy transactionApprovalStrategy;

    @Captor
    ArgumentCaptor<Wallet> walletArgumentCaptor;

    @Captor
    ArgumentCaptor<Transaction> transactionArgumentCaptor;


    @Test
    @DisplayName("manageApproval should throw exception when transaction not found")
    void manageApproval_should_throw_exception_when_transaction_not_found() {
        Long transactionId = 1L;
        when(transactionService.getTransactionByIdWithWallet(transactionId))
                .thenReturn(Optional.empty());

        ApprovalTransactionDto approvalTransactionDto = ApprovalTransactionDto.builder()
                .transactionId(transactionId)
                .build();

        assertThatThrownBy(() -> transactionApprovalService.manageApproval(approvalTransactionDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Transaction not found for given ID: " + transactionId);

        verifyNoInteractions(transactionApprovalFactory, walletService);
    }

    @Test
    @DisplayName("manageApproval should throw exception when transaction is already in the desired state")
    void manageApproval_should_throw_exception_when_transaction_is_already_in_desired_state() {
        Long transactionId = 1L;
        GetTransactionWithWalletDto getTransactionWithWalletDto = GetTransactionWithWalletDto.builder()
                .id(transactionId)
                .status(TransactionStatusType.APPROVED)
                .build();

        when(transactionService.getTransactionByIdWithWallet(transactionId))
                .thenReturn(Optional.of(getTransactionWithWalletDto));

        ApprovalTransactionDto approvalTransactionDto = ApprovalTransactionDto.builder()
                .transactionId(transactionId)
                .build();

        assertThatThrownBy(() -> transactionApprovalService.manageApproval(approvalTransactionDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Transaction is already in the desired state: " + getTransactionWithWalletDto.status());

        verifyNoInteractions(transactionApprovalFactory, walletService);
    }

    @Test
    @DisplayName("manageApproval should handle approval process properly")
    void manageApproval_should_handle_approval_process_properly() {
        Long transactionId = 1L;
        Wallet wallet = Wallet.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(100))
                .build();
        GetTransactionWithWalletDto getTransactionWithWalletDto = GetTransactionWithWalletDto.builder()
                .id(transactionId)
                .status(TransactionStatusType.PENDING)
                .wallet(wallet)
                .build();

        when(transactionService.getTransactionByIdWithWallet(transactionId))
                .thenReturn(Optional.of(getTransactionWithWalletDto));

        ApprovalTransactionDto approvalTransactionDto = ApprovalTransactionDto.builder()
                .transactionId(transactionId)
                .status(TransactionStatusType.APPROVED)
                .build();

        when(transactionApprovalFactory.getStrategy(approvalTransactionDto.status())).thenReturn(transactionApprovalStrategy);

        transactionApprovalService.manageApproval(approvalTransactionDto);

        verify(transactionApprovalStrategy, times(1)).handle(transactionArgumentCaptor.capture(), walletArgumentCaptor.capture());
        assertThat(transactionArgumentCaptor.getValue().getId()).isEqualTo(getTransactionWithWalletDto.id());
        assertThat(walletArgumentCaptor.getValue().getId()).isEqualTo(getTransactionWithWalletDto.wallet().getId());

        verify(transactionService, times(1)).updateTransactionStatus(getTransactionWithWalletDto.id(), approvalTransactionDto.status());

        verify(walletService, times(1)).upsertWallet(walletArgumentCaptor.capture());
        assertThat(walletArgumentCaptor.getValue().getId()).isEqualTo(getTransactionWithWalletDto.wallet().getId());
    }
}