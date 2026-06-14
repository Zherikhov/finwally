package com.myeasybudget.shared.application;

/**
 * Thrown when a requested resource does not exist or is not visible to the current
 * user (e.g. it belongs to another account or has been soft-deleted). Mapped to HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
