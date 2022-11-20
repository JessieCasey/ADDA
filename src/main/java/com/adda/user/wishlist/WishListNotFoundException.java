package com.adda.user.wishlist;

/**
 * The WishListNotFoundException exception extends RuntimeException.
 * GlobalExceptionHandler handles this exception. {@link com.adda.exception.GlobalExceptionHandler}
 */

public class WishListNotFoundException extends RuntimeException {

    public WishListNotFoundException(String message) {
        super(message);
    }
}
