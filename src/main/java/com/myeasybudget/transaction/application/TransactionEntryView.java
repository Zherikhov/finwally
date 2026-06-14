package com.myeasybudget.transaction.application;

import com.myeasybudget.transaction.infrastructure.persistence.TransactionEntryEntity;
import java.math.BigDecimal;
import java.util.UUID;

public record TransactionEntryView(
        UUID id,
        UUID walletId,
        String walletName,
        UUID categoryId,
        String categoryName,
        BigDecimal amount,
        String currencyCode,
        String note
) {

    public static TransactionEntryView from(TransactionEntryEntity entry) {
        return new TransactionEntryView(
                entry.getId(),
                entry.getWallet() == null ? null : entry.getWallet().getId(),
                entry.getWallet() == null ? null : entry.getWallet().getName(),
                entry.getCategory() == null ? null : entry.getCategory().getId(),
                entry.getCategory() == null ? null : entry.getCategory().getName(),
                entry.getAmount(),
                entry.getCurrencyCode(),
                entry.getNote()
        );
    }
}
