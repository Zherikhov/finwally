package com.myeasybudget.transaction.application;

import com.myeasybudget.transaction.infrastructure.persistence.TransactionStatus;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionType;
import java.time.OffsetDateTime;
import java.util.List;

public record UpdateTransactionCommand(
        TransactionType type,
        TransactionStatus status,
        OffsetDateTime occurredAt,
        String title,
        String description,
        String merchantName,
        List<EntryCommand> entries
) {
}
