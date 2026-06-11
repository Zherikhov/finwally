package com.finwally.transaction.infrastructure.persistence;

public enum TransactionSource {
    MANUAL,
    CSV_IMPORT,
    OPEN_BANKING,
    QR_RECEIPT,
    SYSTEM
}
