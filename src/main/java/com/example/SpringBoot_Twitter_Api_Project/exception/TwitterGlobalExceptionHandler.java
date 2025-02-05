package com.example.SpringBoot_Twitter_Api_Project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class TwitterGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<TwitterErrorResponse> handleException(TwitterException exception) {
        TwitterErrorResponse errorResponse = new TwitterErrorResponse(
                exception.getHttpStatus().value(),
                exception.getLocalizedMessage(),
                System.currentTimeMillis()
        );

        log.error("Exception occurred: ", exception);

        return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<TwitterErrorResponse> handleException(Exception exception) {
        TwitterErrorResponse errorResponse = new TwitterErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getLocalizedMessage(),
                System.currentTimeMillis()
        );

        log.error("Exception occurred: ", exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
