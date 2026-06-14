package com.myeasybudget.shared.application;

/**
 * Thrown when a request is syntactically valid but violates a domain rule
 * (e.g. an unbalanced transfer, a category whose type does not match its parent).
 * Mapped to HTTP 422 (Unprocessable Entity).
 */
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
