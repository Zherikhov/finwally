package com.myeasybudget.account.application;

import com.myeasybudget.account.infrastructure.persistence.WalletEntity;
import com.myeasybudget.account.infrastructure.persistence.WalletType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record WalletView(
        UUID id,
        String name,
        WalletType type,
        String currencyCode,
        BigDecimal openingBalance,
        LocalDate openingBalanceDate,
        BigDecimal currentBalance,
        boolean isDefault,
        int sortOrder,
        OffsetDateTime archivedAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static WalletView from(WalletEntity wallet, BigDecimal currentBalance) {
        return new WalletView(
                wallet.getId(),
                wallet.getName(),
                wallet.getType(),
                wallet.getCurrencyCode(),
                wallet.getOpeningBalance(),
                wallet.getOpeningBalanceDate(),
                currentBalance,
                Boolean.TRUE.equals(wallet.getIsDefault()),
                wallet.getSortOrder() == null ? 0 : wallet.getSortOrder(),
                wallet.getArchivedAt(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }
}
