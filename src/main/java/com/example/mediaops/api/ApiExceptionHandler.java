package com.example.mediaops.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.Map;

/**
 * Centralized exception -> HTTP response mapping.
 *
 * @RestControllerAdvice is a global filter applied to every @RestController.
 * Each @ExceptionHandler maps a thrown exception to a structured JSON body.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> notFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "not_found", "message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "invalid_state", "message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of(
                "error", "validation_failed",
                "fields", ex.getBindingResult().getFieldErrors().stream()
                    .map(f -> Map.of(
                        "field", f.getField(),
                        "message", f.getDefaultMessage() == null ? "invalid" : f.getDefaultMessage()
                    ))
                    .toList()
            ));
    }
}
