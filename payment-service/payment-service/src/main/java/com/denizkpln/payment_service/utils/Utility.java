package com.denizkpln.payment_service.utils;

import com.denizkpln.payment_service.model.Transaction;
import com.example.grpccommon.TransactionDetail;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class Utility {

    public static List<Transaction> fetchTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        // Mock data for transaction details
        Transaction transaction1 = new Transaction("1", "Deposit", 100.0f);
        Transaction transaction2 = new Transaction("2", "Withdrawal", 50.0f);
        Transaction transaction3 = new Transaction("3", "Transfer", 75.0f);
        Transaction transaction4 = new Transaction("4", "Deposit", 200.0f);
        Transaction transaction5 = new Transaction("5", "Withdrawal", 30.0f);

        transactions.addAll(Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5));

        return transactions;

    }

    public static TransactionDetail createTransactionDetailFromTransaction(Transaction transaction) {
        return TransactionDetail.newBuilder().setTransactionId(transaction.getId())
                .setTransactionType(transaction.getType()).setTransactionAmount(transaction.getAmount()).build();
    }
}
