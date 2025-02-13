package com.example.SpringBoot_Twitter_Api_Project.integration.tests;

import com.example.SpringBoot_Twitter_Api_Project.dto.RetweetDTO;
import com.example.SpringBoot_Twitter_Api_Project.dto.TweetDTO;
import com.example.SpringBoot_Twitter_Api_Project.dto.UserDTO;
import com.example.SpringBoot_Twitter_Api_Project.service.RetweetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RetweetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetweetService retweetService;

    @Autowired
    private ObjectMapper objectMapper;

    private RetweetDTO retweetDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");


        retweetDTO = new RetweetDTO();
        retweetDTO.setId(1L);
        retweetDTO.setTweetId(1L);
        retweetDTO.setUser(userDTO);
    }

    @Test
    @DisplayName("Create Retweet - Success")
    @WithMockUser(username = "testuser")
    void createRetweet_Success() throws Exception {
        given(retweetService.addRetweet(anyLong(), anyString()))
                .willReturn(retweetDTO);

        mockMvc.perform(post("/retweet/{tweetId}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.user.username", is("testuser")))
                .andExpect(jsonPath("$.tweetId", is(1)));
    }

    @Test
    @DisplayName("Delete Retweet - Success")
    @WithMockUser(username = "testuser")
    void deleteRetweet_Success() throws Exception {
        mockMvc.perform(delete("/retweet/{retweetId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Retweet successfully deleted."));
    }

    @Test
    @DisplayName("Create Retweet - Unauthorized")
    void createRetweet_Unauthorized() throws Exception {
        mockMvc.perform(post("/retweet/{tweetId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Delete Retweet - Unauthorized")
    void deleteRetweet_Unauthorized() throws Exception {
        mockMvc.perform(delete("/retweet/{retweetId}", 1L))
                .andExpect(status().isUnauthorized());
    }

}
