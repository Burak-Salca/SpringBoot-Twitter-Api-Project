package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.dto.TweetDTO;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.service.TweetService;
import com.example.SpringBoot_Twitter_Api_Project.dto.TweetRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @PostMapping("/createTweet")
    public ResponseEntity<?> createTweet(@Valid @RequestBody TweetRequest request, Authentication authentication) {
        String currentUsername = authentication.getName();
        return  tweetService.createTweet(request.getContent(), currentUsername);
    }

    @PutMapping("updateTweet/{id}")
    public ResponseEntity<?> updateTweet( @PathVariable Long id, @Valid @RequestBody TweetRequest request, Authentication authentication) {
        return tweetService.updateTweet(id, request.getContent(), authentication.getName());
    }

    @DeleteMapping("deleteTweet/{id}")
    public ResponseEntity<?> deleteTweet(@PathVariable Long id, Authentication authentication) {
        return tweetService.deleteTweet(id, authentication.getName());
    }

    @GetMapping("/getByIdTweet/{id}")
    public TweetDTO getByIdTweet(@RequestParam Long id) {
        return tweetService.getByIdTweet(id);
    }

    @GetMapping("/getTweetsByUserId/{id}")
    public List<TweetDTO> getTweetsByUserId(@PathVariable Long id) {
        return tweetService.getTweetsByUserId(id);
    }




}
