package com.cagan.walletapi.mapper;

import com.cagan.walletapi.dto.CreateTransactionDto;
import com.cagan.walletapi.dto.MakeWithdrawDto;
import com.cagan.walletapi.rest.request.MakeWithdrawRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WithdrawMapper {

    WithdrawMapper INSTANCE = Mappers.getMapper(WithdrawMapper.class);

    MakeWithdrawDto toMakeWithdrawDto(MakeWithdrawRequest request);

    @Mapping(target = "type", constant = "WITHDRAW")
    CreateTransactionDto toCreateTransactionDto(MakeWithdrawDto makeWithdrawDto);
}
