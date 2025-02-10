package com.example.SpringBoot_Twitter_Api_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetweetDTO {
    private Long id;
    private UserDTO user;
}