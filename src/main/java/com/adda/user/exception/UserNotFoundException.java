package com.adda.user.exception;

/**
 * The UserNotFoundException exception extends RuntimeException.
 * GlobalExceptionHandler handles this exception. {@link com.adda.exception.GlobalExceptionHandler}
 */

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
