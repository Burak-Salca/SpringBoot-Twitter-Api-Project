package com.example.SpringBoot_Twitter_Api_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private List<CommentDTO> comments;
    private List<LikeDTO> likes;
    private List<RetweetDTO> retweets;
}
