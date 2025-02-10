package com.example.SpringBoot_Twitter_Api_Project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor

public class TweetRequest {
    @NotNull(message = "İçerik boş olamaz")
    @Size(min = 1, max = 280, message = "Tweet 1-280 karakter arasında olmalıdır")
    private String content;

    public TweetRequest(String content) {
        this.content = content;
    }
} 