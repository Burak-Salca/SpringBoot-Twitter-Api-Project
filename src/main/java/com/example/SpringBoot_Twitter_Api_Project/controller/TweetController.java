package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.service.TweetService;
import com.example.SpringBoot_Twitter_Api_Project.dto.TweetRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @PostMapping
    public ResponseEntity<?> createTweet(@Valid @RequestBody TweetRequest request, Authentication authentication) {
        String username = authentication.getName(); // giriş yapmış kullanıcının username'ini alır
        Tweet tweet = tweetService.createTweet(request.getContent(), username);
        return ResponseEntity.ok(tweet);
    }

    @GetMapping("/findById")
    public Tweet findById(@RequestParam Long id) {
        return tweetService.findById(id);
    }

    @PutMapping("/{id}")
    public Tweet updateTweet(@PathVariable Long id, @RequestBody String newTweet, Authentication authentication) {
        return tweetService.updateTweet(id, newTweet, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public String deleteTweet(@PathVariable Long id, Authentication authentication) {
        tweetService.deleteTweet(id, authentication.getName());
        return "Tweet deleted successfully.";
    }
}
