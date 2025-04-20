package com.cagan.walletapi.rest.controller;

import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.mapper.WalletMapper;
import com.cagan.walletapi.rest.request.CreateWalletRequest;
import com.cagan.walletapi.rest.response.GetWalletResponse;
import com.cagan.walletapi.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletMapper walletMapper = WalletMapper.INSTANCE;

    private final WalletService walletService;

    @PostMapping
    ResponseEntity<GetWalletResponse> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        CreateWalletDto createWalletDto = walletMapper.toCreateWalletDto(request);
        GetWalletDto getWalletDto = walletService.createWallet(createWalletDto);
        GetWalletResponse response = walletMapper.toGetWalletResponse(getWalletDto);
        return ResponseEntity.ok(response);
    }

}
