package com.myeasybudget.user.application;

public record RegisterCommand(
        String email,
        String password,
        String displayName,
        String defaultCurrencyCode,
        String locale,
        String timezone
) {
}
