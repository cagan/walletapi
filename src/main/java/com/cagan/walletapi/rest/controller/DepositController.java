package com.cagan.walletapi.rest.controller;

import com.cagan.walletapi.dto.MakeDepositDto;
import com.cagan.walletapi.mapper.DepositMapper;
import com.cagan.walletapi.rest.request.MakeDepositRequest;
import com.cagan.walletapi.security.CustomUserDetails;
import com.cagan.walletapi.service.deposit.DepositService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deposits")
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;
    private final DepositMapper depositMapper = DepositMapper.INSTANCE;

    @PostMapping
    ResponseEntity<Void> makeDeposit(@Valid @RequestBody MakeDepositRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        MakeDepositDto makeDepositDto = depositMapper.toMakeDepositDto(request);
        makeDepositDto = makeDepositDto.toBuilder().role(user.getRole()).customerId(user.getCustomerId()).build();
        depositService.makeDeposit(makeDepositDto);
        return ResponseEntity.ok().build();
    }
}
