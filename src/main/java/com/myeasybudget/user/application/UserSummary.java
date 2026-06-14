package com.myeasybudget.user.application;

import com.myeasybudget.user.infrastructure.persistence.AppUserEntity;
import com.myeasybudget.user.infrastructure.persistence.UserStatus;
import java.util.UUID;

public record UserSummary(
        UUID id,
        String email,
        String displayName,
        String defaultCurrencyCode,
        String locale,
        String timezone,
        UserStatus status
) {

    public static UserSummary from(AppUserEntity user) {
        return new UserSummary(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getDefaultCurrencyCode(),
                user.getLocale(),
                user.getTimezone(),
                user.getStatus()
        );
    }
}
