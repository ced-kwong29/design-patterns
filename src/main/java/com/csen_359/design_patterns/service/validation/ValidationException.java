package com.csen_359.design_patterns.service.validation;

/**
 * Thrown by a validation handler when an incoming usage entry is rejected.
 * Surfaces as HTTP 400 (see the controller advice / handler in the controller
 * package).
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
