package com.example.SpringBoot_Twitter_Api_Project.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TwitterException extends RuntimeException {

    private HttpStatus httpStatus;

    public TwitterException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
