package com.myeasybudget.transaction.presentation;

import com.myeasybudget.transaction.application.EntryCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record EntryRequest(
        @NotNull
        UUID walletId,

        UUID categoryId,

        @NotNull
        BigDecimal amount,

        @Pattern(regexp = "^[A-Za-z]{3}$")
        String currencyCode,

        @Size(max = 2000)
        String note
) {

    public EntryCommand toCommand() {
        return new EntryCommand(walletId, categoryId, amount, currencyCode, note);
    }
}
