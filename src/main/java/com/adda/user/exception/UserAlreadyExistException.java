package com.adda.user.exception;

/**
 * The UserAlreadyExistException exception extends RuntimeException.
 * GlobalExceptionHandler handles this exception. {@link com.adda.exception.GlobalExceptionHandler}
 */

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
