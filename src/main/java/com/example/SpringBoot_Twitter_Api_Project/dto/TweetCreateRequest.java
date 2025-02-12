package com.example.SpringBoot_Twitter_Api_Project.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetCreateRequest {
    @Size(min = 1, max = 280, message = "Tweet must be between 1 and 280 characters")
    private String content;
} 