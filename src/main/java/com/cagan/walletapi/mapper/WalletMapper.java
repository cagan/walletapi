package com.cagan.walletapi.mapper;

import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.SearchWalletDto;
import com.cagan.walletapi.rest.request.CreateWalletRequest;
import com.cagan.walletapi.rest.request.SearchWalletRequest;
import com.cagan.walletapi.rest.response.GetWalletResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(target = "currency", expression = "java(com.cagan.walletapi.util.enums.CurrencyType.fromValue(createWalletDto.currency()))")
    Wallet toEntity(CreateWalletDto createWalletDto);

    @Mapping(target = "customerId", source = "wallet.customer.id")
    GetWalletDto toGetWalletDto(Wallet wallet);

    List<GetWalletDto> toGetWalletDtoList(List<Wallet> wallets);

    CreateWalletDto toCreateWalletDto(CreateWalletRequest request, Long userId);

    GetWalletResponse toGetWalletResponse(GetWalletDto getWalletDto);

    List<GetWalletResponse> toGetWalletResponses(List<GetWalletDto> getWalletDtoList);

    SearchWalletDto toSearchWalletDto(SearchWalletRequest request);
}
