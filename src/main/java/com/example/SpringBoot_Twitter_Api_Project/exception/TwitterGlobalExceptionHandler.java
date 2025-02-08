package com.example.SpringBoot_Twitter_Api_Project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<TwitterErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        TwitterErrorResponse errorResponse = new TwitterErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Bu işlemi gerçekleştirmek için yetkiniz yok. Lütfen giriş yapın.",
            System.currentTimeMillis()
        );

        log.error("Access Denied Exception occurred: ", exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<TwitterErrorResponse> handleAuthenticationException(AuthenticationException exception) {
        TwitterErrorResponse errorResponse = new TwitterErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Kimlik doğrulama başarısız. Lütfen tekrar giriş yapın.",
            System.currentTimeMillis()
        );

        log.error("Authentication Exception occurred: ", exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<TwitterErrorResponse> handleJwtException(JwtException exception) {
        TwitterErrorResponse errorResponse = new TwitterErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Token hatası: " + exception.getLocalizedMessage(),
            System.currentTimeMillis()
        );

        log.error("JWT Exception occurred: ", exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

}
