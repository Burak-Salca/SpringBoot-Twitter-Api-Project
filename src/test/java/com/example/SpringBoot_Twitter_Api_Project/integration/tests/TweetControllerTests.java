package com.example.SpringBoot_Twitter_Api_Project.integration.tests;

import com.example.SpringBoot_Twitter_Api_Project.controller.TweetController;
import com.example.SpringBoot_Twitter_Api_Project.dto.TweetCreateRequest;
import com.example.SpringBoot_Twitter_Api_Project.dto.TweetDTO;
import com.example.SpringBoot_Twitter_Api_Project.service.TweetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TweetController.class)
class TweetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TweetService tweetService;

    @Autowired
    private ObjectMapper objectMapper;

    private TweetDTO tweetDTO;
    private TweetCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new TweetCreateRequest();
        createRequest.setContent("Test tweet");

        tweetDTO = new TweetDTO();
        tweetDTO.setId(1L);
        tweetDTO.setContent("Test tweet");
    }

    @Test
    @DisplayName("Create Tweet - Success")
    @WithMockUser(username = "testUser")
    void createTweet_Success() throws Exception {
        given(tweetService.createTweet(anyString(), anyString()))
                .willReturn(tweetDTO);

        mockMvc.perform(post("/tweets")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())  // CSRF token eklendi
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is(createRequest.getContent())));
    }

    @Test
    @DisplayName("Get Tweet By Id - Success")
    @WithMockUser(username = "testUser") 
    void getTweetById_Success() throws Exception {
        given(tweetService.getByIdTweet(1L)).willReturn(tweetDTO);

        mockMvc.perform(get("/tweets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Test tweet")));
    }

    @Test
    @DisplayName("Delete Tweet - Success")
    @WithMockUser(username = "testUser")
    void deleteTweet_Success() throws Exception {
        mockMvc.perform(delete("/tweets/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))  // CSRF token eklendi
                .andExpect(status().isOk())
                .andExpect(content().string("Tweet successfully deleted."));
    }

    @Test
    @DisplayName("Update Tweet - Success")
    @WithMockUser(username = "testUser")
    void updateTweet_Success() throws Exception {
        given(tweetService.updateTweet(any(), any(), any())).willReturn(tweetDTO);

        mockMvc.perform(put("/tweets/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())  // CSRF token eklendi
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Test tweet")));
    }
}