package com.example.SpringBoot_Twitter_Api_Project.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @Size(min = 1, max = 280, message = "Comment 1-280 karakter arasında olmalıdır")
    private String content;
} 