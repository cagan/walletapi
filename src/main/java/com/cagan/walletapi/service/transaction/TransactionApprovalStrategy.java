package com.cagan.walletapi.service.transaction;

import com.cagan.walletapi.data.entity.Transaction;
import com.cagan.walletapi.data.entity.Wallet;
import com.cagan.walletapi.util.enums.TransactionStatusType;

public interface TransactionApprovalStrategy {

    void handle(Transaction transaction, Wallet wallet);

    TransactionStatusType getTransactionStatusType();

}
