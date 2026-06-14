package com.myeasybudget.user.application;

import java.time.Instant;

public record AuthResult(
        String accessToken,
        Instant expiresAt,
        UserSummary user
) {
}
