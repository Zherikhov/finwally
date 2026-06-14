package com.myeasybudget.transaction.application;

import com.myeasybudget.transaction.infrastructure.persistence.TransactionSource;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionStatus;
import com.myeasybudget.transaction.infrastructure.persistence.TransactionType;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Optional filters for browsing/searching transaction history. Any null field is
 * ignored. {@code query} is a free-text match over title, description and merchant.
 */
public record TransactionSearchCriteria(
        TransactionType type,
        TransactionStatus status,
        TransactionSource source,
        UUID walletId,
        UUID categoryId,
        LocalDate dateFrom,
        LocalDate dateTo,
        String query
) {
}
