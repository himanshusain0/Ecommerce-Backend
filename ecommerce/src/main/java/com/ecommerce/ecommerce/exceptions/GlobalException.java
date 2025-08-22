package com.ecommerce.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetails> userExceptionHandler(UserException ue, WebRequest req) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(ue.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException se, WebRequest req) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(se.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> productExceptionHandler(ProductException pe, WebRequest req) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(pe.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    // Optional: Add a generic exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> genericExceptionHandler(Exception e, WebRequest req) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError("An unexpected error occurred: " + e.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
}