package com.cagan.walletapi.mapper;

import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.rest.request.CreateWalletRequest;
import com.cagan.walletapi.rest.response.GetWalletResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(target = "currency", expression = "java(com.cagan.walletapi.util.enums.CurrencyType.fromValue(createWalletDto.currency()))")
    Wallet toEntity(CreateWalletDto createWalletDto);

    GetWalletDto toGetWalletDto(Wallet savedWallet);

    CreateWalletDto toCreateWalletDto(CreateWalletRequest request);

    GetWalletResponse toGetWalletResponse(GetWalletDto getWalletDto);

}
