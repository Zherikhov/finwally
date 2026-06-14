package com.myeasybudget.account.presentation;

import com.myeasybudget.account.application.UpdateWalletCommand;
import com.myeasybudget.account.infrastructure.persistence.WalletType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateWalletRequest(
        @NotBlank
        @Size(max = 120)
        String name,

        @NotNull
        WalletType type,

        @NotBlank
        @Pattern(regexp = "^[A-Za-z]{3}$")
        String currencyCode,

        BigDecimal openingBalance,

        LocalDate openingBalanceDate,

        boolean isDefault,

        Integer sortOrder
) {

    public UpdateWalletCommand toCommand() {
        return new UpdateWalletCommand(
                name, type, currencyCode, openingBalance, openingBalanceDate, isDefault, sortOrder);
    }
}
