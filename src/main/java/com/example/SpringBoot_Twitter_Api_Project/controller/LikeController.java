package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Like;
import com.example.SpringBoot_Twitter_Api_Project.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<Like> likeTweet(@PathVariable Long userId, @PathVariable Long tweetId) {
        Like likedTweet = likeService.addlike(userId, tweetId);
        return new ResponseEntity<>(likedTweet, HttpStatus.CREATED);
    }

    @PostMapping("/dislike")
    public ResponseEntity<String> dislikeTweet(@RequestParam Long tweetId, @RequestParam Long userId) {
        likeService.removeLike(tweetId, userId);
        return new ResponseEntity<>("Like removed successfully.", HttpStatus.OK);
    }
}
