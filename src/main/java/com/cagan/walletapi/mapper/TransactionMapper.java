package com.cagan.walletapi.mapper;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.dto.ApprovalTransactionDto;
import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import com.cagan.walletapi.dto.GetTransactionWithWalletDto;
import com.cagan.walletapi.rest.request.ApprovalTransactionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "wallet.id", source = "walletId")
    Transaction toEntity(CreateTransactionDto createTransactionDto);

    Transaction toEntity(GetTransactionWithWalletDto getTransactionWithWalletDto);

    @Mapping(target = "walletId", source = "wallet.id")
    GetTransactionDto toGetTransactionDto(Transaction transaction);

    GetTransactionWithWalletDto toGetTransactionWithWalletDto(Transaction transaction);

    List<GetTransactionDto> toGetTransactionDtoList(List<Transaction> transaction);

    ApprovalTransactionDto toApprovalTransactionDto(ApprovalTransactionRequest request);
}
