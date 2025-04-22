package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.util.enums.TransactionStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TransactionApprovalFactory {
    private final Map<TransactionStatusType, TransactionApprovalStrategy> strategyMap;

    @Autowired
    public TransactionApprovalFactory(List<TransactionApprovalStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(TransactionApprovalStrategy::getTransactionStatusType, Function.identity()));
    }

    public TransactionApprovalStrategy getStrategy(TransactionStatusType statusType) {
        return strategyMap.get(statusType);
    }
}