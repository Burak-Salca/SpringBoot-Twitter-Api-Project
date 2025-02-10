package com.example.SpringBoot_Twitter_Api_Project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class TweeterGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<TweeterErrorResponse> handleException(TweeterException exception) {
        TweeterErrorResponse errorResponse = new TweeterErrorResponse(
                exception.getHttpStatus().value(),
                exception.getLocalizedMessage(),
                System.currentTimeMillis()
        );

        log.error("Exception occurred: ", exception);

        return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<TweeterErrorResponse> handleException(Exception exception) {
        TweeterErrorResponse errorResponse = new TweeterErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getLocalizedMessage(),
                System.currentTimeMillis()
        );

        log.error("Exception occurred: ", exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // İlk validation hatasının mesajını al
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.badRequest().body(errorMessage);
    }



}
