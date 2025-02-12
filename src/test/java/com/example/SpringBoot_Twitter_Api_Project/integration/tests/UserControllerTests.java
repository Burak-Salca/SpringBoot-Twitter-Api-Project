package com.example.SpringBoot_Twitter_Api_Project.integration.tests;

import com.example.SpringBoot_Twitter_Api_Project.config.SecurityConfig;
import com.example.SpringBoot_Twitter_Api_Project.controller.UserController;
import com.example.SpringBoot_Twitter_Api_Project.dto.LoginRequest;
import com.example.SpringBoot_Twitter_Api_Project.dto.RegisterRequest;
import com.example.SpringBoot_Twitter_Api_Project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        // Register request için test verisi
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("Test123!");


        // Login request için test verisi
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("Test123!");
    }

    @Test
    @DisplayName("Register - Success")
    void register_Success() throws Exception {
        given(userService.register(any(RegisterRequest.class)))
                .willReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Register - Invalid Request")
    void register_InvalidRequest() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest();
        // Boş request ile test

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login - Success")
    void login_Success() throws Exception {
        given(userService.login(any(LoginRequest.class)))
                .willReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Login - Invalid Credentials")
    void login_InvalidCredentials() throws Exception {
        LoginRequest invalidLogin = new LoginRequest();
        invalidLogin.setUsername("wronguser");
        invalidLogin.setPassword("wrongpass");

        given(userService.login(any(LoginRequest.class)))
                .willReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().isBadRequest());
    }
}