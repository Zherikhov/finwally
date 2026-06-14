package com.myeasybudget.account.application;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Sum of POSTED transaction entries for a single wallet. Built directly by a JPQL
 * constructor expression in the entry repository.
 */
public record WalletBalanceView(UUID walletId, BigDecimal total) {
}
