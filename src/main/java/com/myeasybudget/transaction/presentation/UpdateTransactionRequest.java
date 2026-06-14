package com.myeasybudget.transaction.presentation;

import com.myeasybudget.transaction.application.UpdateTransactionCommand;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionStatus;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

public record UpdateTransactionRequest(
        @NotNull
        TransactionType type,

        TransactionStatus status,

        @NotNull
        OffsetDateTime occurredAt,

        @Size(max = 255)
        String title,

        String description,

        @Size(max = 255)
        String merchantName,

        @NotEmpty
        @Valid
        List<EntryRequest> entries
) {

    public UpdateTransactionCommand toCommand() {
        return new UpdateTransactionCommand(
                type,
                status,
                occurredAt,
                title,
                description,
                merchantName,
                entries.stream().map(EntryRequest::toCommand).toList()
        );
    }
}
