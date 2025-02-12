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
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity<TweetDTO> createTweet(@Valid @RequestBody TweetRequest request, Authentication authentication) {
        TweetDTO createdTweet = tweetService.createTweet(request.getContent(), authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTweet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetDTO> getTweetById(@PathVariable Long id) {
        TweetDTO tweet = tweetService.getByIdTweet(id);
        return ResponseEntity.ok(tweet);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TweetDTO>> getTweetsByUserId(@PathVariable Long userId) {
        List<TweetDTO> tweets = tweetService.getTweetsByUserId(userId);
        return ResponseEntity.ok(tweets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetDTO> updateTweet(@PathVariable Long id, @Valid @RequestBody TweetRequest request, Authentication authentication) {
        TweetDTO updatedTweet = tweetService.updateTweet(id, request.getContent(), authentication.getName());
        return ResponseEntity.ok(updatedTweet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTweet(@PathVariable Long id, Authentication authentication) {
        tweetService.deleteTweet(id, authentication.getName());
        return ResponseEntity.ok("Tweet successfully deleted.");
    }
}
