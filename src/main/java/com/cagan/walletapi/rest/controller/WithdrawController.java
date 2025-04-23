package com.cagan.walletapi.rest.controller;

import com.cagan.walletapi.dto.MakeWithdrawDto;
import com.cagan.walletapi.mapper.WithdrawMapper;
import com.cagan.walletapi.rest.request.MakeWithdrawRequest;
import com.cagan.walletapi.security.CustomUserDetails;
import com.cagan.walletapi.service.withdraw.WithdrawService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/withdraws")
@RequiredArgsConstructor
public class WithdrawController {
    private final WithdrawService withdrawService;

    private final WithdrawMapper withdrawMapper = WithdrawMapper.INSTANCE;

    @PostMapping
    ResponseEntity<Void> makeWithdraw(@Valid @RequestBody MakeWithdrawRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        MakeWithdrawDto makeWithdrawDto = withdrawMapper.toMakeWithdrawDto(request);
        makeWithdrawDto = makeWithdrawDto.toBuilder().role(user.getRole()).customerId(user.getCustomerId()).build();
        withdrawService.makeWithdraw(makeWithdrawDto);
        return ResponseEntity.ok().build();
    }
}
