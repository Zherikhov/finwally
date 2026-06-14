package com.myeasybudget.account.application;

import com.myeasybudget.account.infrastructure.persistence.WalletType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateWalletCommand(
        String name,
        WalletType type,
        String currencyCode,
        BigDecimal openingBalance,
        LocalDate openingBalanceDate,
        boolean isDefault,
        Integer sortOrder
) {
}
