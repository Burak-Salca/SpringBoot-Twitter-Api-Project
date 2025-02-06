package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.service.TweetService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
public class TweetController {
    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity<Tweet> createTweet(@RequestParam Long userId, @RequestBody Tweet tweetRequest) {
        Tweet savedTweet = tweetService.createTweet(userId, tweetRequest.getContent());
        return new ResponseEntity<>(savedTweet, HttpStatus.CREATED);
    }

    @GetMapping("/findById")
    public ResponseEntity<Tweet> findById(@RequestParam Long id) {
        Tweet tweet = tweetService.findById(id);
        return new ResponseEntity<>(tweet, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable Long id, @RequestParam Long userId, @RequestBody String newTweet) {
        Tweet updatedTweet = tweetService.updateTweet(id, userId, newTweet);
        return new ResponseEntity<>(updatedTweet, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteTweet(@PathVariable Long id, @RequestParam Long userId) {
        tweetService.deleteTweet(id,userId);
        return new ResponseEntity<>("Comment successfully deleted.", HttpStatus.OK);
    }
}
