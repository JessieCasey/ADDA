package com.adda.advert.exception;

public class AdvertNotFoundException extends RuntimeException {
    public AdvertNotFoundException(String message) {
        super(message);
    }
}
