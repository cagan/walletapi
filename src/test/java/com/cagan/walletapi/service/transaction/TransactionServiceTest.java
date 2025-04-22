package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.data.repository.TransactionRepository;
import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.util.enums.OppositePartyType;
import com.cagan.walletapi.util.enums.TransactionStatusType;
import com.cagan.walletapi.util.enums.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    @Captor
    ArgumentCaptor<Transaction> transactionArgumentCaptor;


    @Test
    @DisplayName("Create transaction sets status to PENDING when transaction amount exceeds max limit")
    void createTransaction_should_set_transaction_status_to_pending_when_transaction_amount_exceeds_max_limit() {
        CreateTransactionDto createTransactionDto = CreateTransactionDto.builder()
                .amount(TransactionService.MAX_TRANSACTION_AMOUNT.add(BigDecimal.valueOf(1000)))
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(OppositePartyType.IBAN)
                .walletId(1L)
                .build();

        transactionService.createTransaction(createTransactionDto);

        verify(transactionRepository, times(1)).save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue().getStatus()).isEqualTo(TransactionStatusType.PENDING);
        assertThat(transactionArgumentCaptor.getValue().getType()).isEqualTo(createTransactionDto.type());
        assertThat(transactionArgumentCaptor.getValue().getAmount()).isEqualTo(createTransactionDto.amount());
        assertThat(transactionArgumentCaptor.getValue().getWallet().getId()).isEqualTo(createTransactionDto.walletId());
    }


    @Test
    @DisplayName("Create transaction sets status to APPROVED when transaction amount exceeds max limit")
    void createTransaction_should_set_transaction_status_to_APPROVED_when_transaction_amount_exceeds_max_limit() {
        CreateTransactionDto createTransactionDto = CreateTransactionDto.builder()
                .amount(TransactionService.MAX_TRANSACTION_AMOUNT.subtract(BigDecimal.valueOf(1000)))
                .type(TransactionType.WITHDRAW)
                .oppositePartyType(OppositePartyType.IBAN)
                .walletId(1L)
                .build();

        transactionService.createTransaction(createTransactionDto);

        verify(transactionRepository, times(1)).save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue().getStatus()).isEqualTo(TransactionStatusType.APPROVED);
        assertThat(transactionArgumentCaptor.getValue().getType()).isEqualTo(createTransactionDto.type());
        assertThat(transactionArgumentCaptor.getValue().getAmount()).isEqualTo(createTransactionDto.amount());
        assertThat(transactionArgumentCaptor.getValue().getWallet().getId()).isEqualTo(createTransactionDto.walletId());
    }

    @Test
    @DisplayName("Get transactions by wallet ID returns all transactions for given wallet ID")
    void getTransactionsByWalletId_should_return_all_transactions_for_given_wallet_id() {
        long walletId = 1L;

        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .wallet(Wallet.builder().walletName("test one").id(walletId).build())
                .oppositePartyType(OppositePartyType.IBAN)
                .amount(BigDecimal.valueOf(100))
                .type(TransactionType.DEPOSIT) // Add the missing TransactionType
                .status(TransactionStatusType.APPROVED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .wallet(Wallet.builder().walletName("test two").id(walletId).build())
                .oppositePartyType(OppositePartyType.PAYMENT)
                .amount(BigDecimal.valueOf(100000))
                .type(TransactionType.WITHDRAW) // Add the missing TransactionType
                .status(TransactionStatusType.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(transactionRepository.findByWalletIdOrderByCreatedAtDesc(walletId)).thenReturn(List.of(transaction1, transaction2));
        List<GetTransactionDto> result = transactionService.getTransactionsByWalletId(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new GetTransactionDto(transaction1.getId(), transaction1.getWallet().getId(), transaction1.getAmount(), transaction1.getType(), transaction1.getOppositePartyType(), transaction1.getStatus(), transaction1.getCreatedAt(), transaction1.getUpdatedAt()),
                new GetTransactionDto(transaction2.getId(), transaction2.getWallet().getId(), transaction2.getAmount(), transaction2.getType(), transaction2.getOppositePartyType(), transaction2.getStatus(), transaction2.getCreatedAt(), transaction2.getUpdatedAt())
        ));
    }
}