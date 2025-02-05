package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Retweet;
import com.example.SpringBoot_Twitter_Api_Project.service.RetweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final RetweetService retweetService;

    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    @PostMapping
    public ResponseEntity<Retweet> addRetweet(@PathVariable Long tweetId, @PathVariable Long userId) {
        Retweet savedRetweet = retweetService.addRetweet(tweetId, userId);
        return new ResponseEntity<>(savedRetweet, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRetweet(@PathVariable Long id, @RequestParam Long userId) {
        retweetService.deleteRetweet(id, userId);
        return new ResponseEntity<>("Retweet deleted successfully.", HttpStatus.OK);
    }
}
