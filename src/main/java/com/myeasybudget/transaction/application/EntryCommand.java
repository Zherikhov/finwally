package com.myeasybudget.transaction.application;

import java.math.BigDecimal;
import java.util.UUID;

public record EntryCommand(
        UUID walletId,
        UUID categoryId,
        BigDecimal amount,
        String currencyCode,
        String note
) {
}
