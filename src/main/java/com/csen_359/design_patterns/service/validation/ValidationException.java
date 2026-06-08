package com.csen_359.design_patterns.service.validation;

/**
 * Thrown by a validation handler when an incoming usage entry is rejected.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
