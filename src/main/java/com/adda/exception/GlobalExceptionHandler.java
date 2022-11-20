package com.adda.exception;

import com.adda.advert.exception.AdvertNotFoundException;
import com.adda.advice.MessageException;
import com.adda.user.exception.UserAlreadyExistException;
import com.adda.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex,  WebRequest request) {
        log.error("[GH][handleUserNotFoundException]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new MessageException(HttpStatus.NO_CONTENT.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleAdvertNotFound(AdvertNotFoundException ex, WebRequest request) {
        log.error("[GH][handleAdvertNotFound]: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new MessageException(HttpStatus.NO_CONTENT.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleEntityNotFoundException(UserAlreadyExistException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.error("[handleAccessDeniedException] Access denied '" + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .body(new MessageException(HttpStatus.FORBIDDEN.value(), ex.getMessage(), request));
    }
}
