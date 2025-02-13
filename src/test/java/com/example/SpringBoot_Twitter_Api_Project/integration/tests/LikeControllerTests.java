package com.example.SpringBoot_Twitter_Api_Project.integration.tests;

import com.example.SpringBoot_Twitter_Api_Project.dto.LikeDTO;
import com.example.SpringBoot_Twitter_Api_Project.dto.UserDTO;
import com.example.SpringBoot_Twitter_Api_Project.service.LikeService;
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
class LikeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    private LikeDTO likeDTO;

    @BeforeEach
    void setUp() {
        likeDTO = new LikeDTO();
        likeDTO.setId(1L);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        likeDTO.setUser(userDTO);
    }

    @Test
    @DisplayName("Create Like - Success")
    @WithMockUser(username = "testuser")
    void createLike_Success() throws Exception {
        given(likeService.addLike(anyLong(), anyString()))
                .willReturn(likeDTO);

        mockMvc.perform(post("/like/{tweetId}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.user.username", is("testuser")));
    }

    @Test
    @DisplayName("Delete Like - Success")
    @WithMockUser(username = "testuser")
    void deleteLike_Success() throws Exception {
        mockMvc.perform(delete("/like/{likeId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Like successfully deleted."));
    }

    @Test
    @DisplayName("Create Like - Unauthorized")
    void createLike_Unauthorized() throws Exception {
        mockMvc.perform(post("/likes/{tweetId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Delete Like - Unauthorized")
    void deleteLike_Unauthorized() throws Exception {
        mockMvc.perform(delete("/likes/{likeId}", 1L))
                .andExpect(status().isUnauthorized());
    }
}