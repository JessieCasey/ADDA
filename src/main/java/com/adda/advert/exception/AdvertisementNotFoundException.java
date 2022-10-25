package com.adda.advert.exception;

public class AdvertisementNotFoundException extends RuntimeException {
    public AdvertisementNotFoundException(String message) {
        super(message);
    }
}
