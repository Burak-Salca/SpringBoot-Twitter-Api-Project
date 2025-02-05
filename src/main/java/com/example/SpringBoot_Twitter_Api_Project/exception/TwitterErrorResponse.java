package com.example.SpringBoot_Twitter_Api_Project.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwitterErrorResponse {

    private Integer status;
    private String message;
    private Long timestamp;
}
