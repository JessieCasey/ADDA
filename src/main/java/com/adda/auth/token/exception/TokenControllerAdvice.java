package com.adda.auth.token.exception;

import com.adda.advice.MessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MessageException handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new MessageException(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(), request.getDescription(false));
    }
}