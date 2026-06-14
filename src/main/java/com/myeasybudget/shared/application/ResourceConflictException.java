package com.myeasybudget.shared.application;

/**
 * Thrown when a request conflicts with the current state of a resource, such as
 * creating a wallet/category whose name is already taken. Mapped to HTTP 409.
 */
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }
}
