package com.example.SpringBoot_Twitter_Api_Project.integration.tests;


import com.example.SpringBoot_Twitter_Api_Project.dto.CommentRequest;
import com.example.SpringBoot_Twitter_Api_Project.dto.CommentDTO;
import com.example.SpringBoot_Twitter_Api_Project.dto.UserDTO;
import com.example.SpringBoot_Twitter_Api_Project.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;



import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentDTO commentDTO;
    private CommentRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new CommentRequest();
        createRequest.setContent("Test comment");

        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setContent("Test comment");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        commentDTO.setUser(userDTO);
    }

    @Test
    @DisplayName("Create Comment - Success")
    @WithMockUser(username = "testuser")
    void createComment_Success() throws Exception {
        given(commentService.createComment(anyLong(), anyString(), anyString()))
                .willReturn(commentDTO);

        mockMvc.perform(post("/comments/{tweetId}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentRequest("Test comment"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Test comment")))
                .andExpect(jsonPath("$.user.username", is("testuser")));
    }



    @Test
    @DisplayName("Update Comment - Success")
    @WithMockUser(username = "testuser")
    void updateComment_Success() throws Exception {
        given(commentService.updateComment(anyLong(), anyString(), anyString()))
                .willReturn(commentDTO);

        mockMvc.perform(put("/comments/{commentId}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Test comment")))
                .andExpect(jsonPath("$.user.username", is("testuser")));
    }

    @Test
    @DisplayName("Delete Comment - Success")
    @WithMockUser(username = "testuser")
    void deleteComment_Success() throws Exception {
        mockMvc.perform(delete("/comments/{commentId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment successfully deleted."));
    }

    @Test
    @DisplayName("Create Comment - Unauthorized")
    void createComment_Unauthorized() throws Exception {
        mockMvc.perform(post("/comments/{tweetId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Delete Comment - Unauthorized")
    void deleteComment_Unauthorized() throws Exception {
        mockMvc.perform(delete("/comments/{commentId}", 1L))
                .andExpect(status().isUnauthorized());
    }
}