package com.myeasybudget.transaction.application;

import com.myeasybudget.transaction.infrastructure.persistence.FinancialTransactionEntity;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionSource;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionStatus;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionType;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record TransactionView(
        UUID id,
        TransactionType type,
        TransactionStatus status,
        TransactionSource source,
        OffsetDateTime occurredAt,
        LocalDate occurredDate,
        String title,
        String description,
        String merchantName,
        List<TransactionEntryView> entries,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static TransactionView from(FinancialTransactionEntity transaction) {
        List<TransactionEntryView> entries = transaction.getEntries().stream()
                .map(TransactionEntryView::from)
                .toList();
        return new TransactionView(
                transaction.getId(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getSource(),
                transaction.getOccurredAt(),
                transaction.getOccurredDate(),
                transaction.getTitle(),
                transaction.getDescription(),
                transaction.getMerchantName(),
                entries,
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
