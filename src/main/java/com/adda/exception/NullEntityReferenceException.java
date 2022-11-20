package com.adda.exception;

/**
 * The NullEntityReferenceException exception extends RuntimeException.
 * GlobalExceptionHandler handles this exception. {@link com.adda.exception.GlobalExceptionHandler}
 */

public class NullEntityReferenceException extends RuntimeException {
    public NullEntityReferenceException(String message) {
        super(message);
    }
}
