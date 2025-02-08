package com.example.SpringBoot_Twitter_Api_Project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TweetRequest {
    @NotNull(message = "İçerik boş olamaz")
    @Size(min = 1, max = 280, message = "Tweet 1-280 karakter arasında olmalıdır")
    private String content;
} 