package com.cagan.walletapi.mapper;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.GetTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "wallet.id", source = "walletId")
    Transaction toEntity(CreateTransactionDto createTransactionDto);

    @Mapping(target = "walletId", source = "wallet.id")
    GetTransactionDto toGetTransactionDto(Transaction transaction);
}
