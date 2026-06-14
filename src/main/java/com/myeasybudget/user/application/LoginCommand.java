package com.myeasybudget.user.application;

public record LoginCommand(
        String email,
        String password
) {
}
