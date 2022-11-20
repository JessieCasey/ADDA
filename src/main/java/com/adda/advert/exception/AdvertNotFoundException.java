package com.adda.advert.exception;

/**
 * The AdvertNotFoundException exception extends RuntimeException.
 * GlobalExceptionHandler handles this exception. {@link com.adda.exception.GlobalExceptionHandler}
 */

public class AdvertNotFoundException extends RuntimeException {
    public AdvertNotFoundException(String message) {
        super(message);
    }
}
