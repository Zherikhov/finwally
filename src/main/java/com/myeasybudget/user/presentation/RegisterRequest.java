package com.myeasybudget.user.presentation;

import com.myeasybudget.user.application.RegisterCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Email
        @Size(max = 320)
        String email,

        @NotBlank
        @Size(min = 8, max = 72)
        String password,

        @Size(max = 255)
        String displayName,

        @Pattern(regexp = "^[A-Za-z]{3}$")
        String defaultCurrencyCode,

        @Pattern(regexp = "^[A-Za-z]{2,3}([_-][A-Za-z]{2})?$")
        String locale,

        @Size(min = 1, max = 64)
        String timezone
) {

    public RegisterCommand toCommand() {
        return new RegisterCommand(
                email,
                password,
                displayName,
                defaultCurrencyCode == null ? "EUR" : defaultCurrencyCode,
                locale == null ? "en" : locale,
                timezone == null ? "Europe/Berlin" : timezone
        );
    }
}
