package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.dto.LikeDTO;
import com.example.SpringBoot_Twitter_Api_Project.entity.Like;
import com.example.SpringBoot_Twitter_Api_Project.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{tweetId}")
    public ResponseEntity<LikeDTO> likeTweet(@PathVariable Long tweetId, Authentication authentication) {
        LikeDTO likedTweet = likeService.addLike(tweetId, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(likedTweet);
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<LikeDTO> dislikeTweet(@PathVariable Long tweetId, Authentication authentication) {
        LikeDTO dislikedTweet = likeService.removeLike(tweetId, authentication.getName());
        return ResponseEntity.ok(dislikedTweet);
    }
}
