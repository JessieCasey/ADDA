package com.adda.exception;

import com.adda.advert.exception.AdvertNotFoundException;
import com.adda.advice.MessageResponse;
import com.adda.auth.token.exception.TokenRefreshException;
import com.adda.user.exception.UserAlreadyExistException;
import com.adda.user.exception.UserNotFoundException;
import com.adda.user.wishlist.WishListNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex,  WebRequest request) {
        log.error("[GH][handleUserNotFoundException]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new MessageResponse(HttpStatus.NO_CONTENT.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleWishListIsNotException(WishListNotFoundException ex, WebRequest request) {
        log.error("[GH][handleWishListIsNotException]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new MessageResponse(HttpStatus.NO_CONTENT.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleAdvertNotFound(AdvertNotFoundException ex, WebRequest request) {
        log.error("[GH][handleAdvertNotFound]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new MessageResponse(HttpStatus.NO_CONTENT.value(), ex.getMessage(), request));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MessageResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new MessageResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), request);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.error("[handleAccessDeniedException] Access denied '" + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .body(new MessageResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleNullReferenceException(NullEntityReferenceException ex, WebRequest request) {
        log.error("[handleNullReferenceException] Null'" + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(new MessageResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUserAlreadyExistException(UserAlreadyExistException ex, WebRequest request) {
        log.error("[handleUserAlreadyExistException] Null'" + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE.value())
                .body(new MessageResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage(), request));
    }

}
