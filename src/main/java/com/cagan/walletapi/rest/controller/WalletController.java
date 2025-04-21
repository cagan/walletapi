package com.cagan.walletapi.rest.controller;

import com.cagan.walletapi.dto.CreateWalletDto;
import com.cagan.walletapi.dto.GetWalletDto;
import com.cagan.walletapi.dto.SearchWalletDto;
import com.cagan.walletapi.mapper.WalletMapper;
import com.cagan.walletapi.rest.request.CreateWalletRequest;
import com.cagan.walletapi.rest.request.SearchWalletRequest;
import com.cagan.walletapi.rest.response.GetWalletResponse;
import com.cagan.walletapi.service.wallet.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
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

    @PostMapping("/search")
    ResponseEntity<List<GetWalletResponse>> searchWallets(@Valid @RequestBody SearchWalletRequest request) {
        SearchWalletDto searchWalletDto = walletMapper.toSearchWalletDto(request);
        List<GetWalletDto> searchWallets = walletService.searchWallets(searchWalletDto);
        List<GetWalletResponse> responses = walletMapper.toGetWalletResponses(searchWallets);
        return ResponseEntity.ok(responses);
    }
}
