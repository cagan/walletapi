package com.cagan.walletapi.mapper;

import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.MakeDepositDto;
import com.cagan.walletapi.rest.request.MakeDepositRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DepositMapper {
    DepositMapper INSTANCE = Mappers.getMapper(DepositMapper.class);

    MakeDepositDto toMakeDepositDto(MakeDepositRequest request);

    @Mapping(target = "type", constant = "DEPOSIT")
    CreateTransactionDto toCreateTransactionDto(MakeDepositDto makeDepositDto);
}
