package com.example.SpringBoot_Twitter_Api_Project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Kullanıcı adı gereklidir")
    private String username;
    
    @NotBlank(message = "Şifre gereklidir")
    private String password;
} 