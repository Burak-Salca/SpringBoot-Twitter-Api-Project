package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.dto.RetweetDTO;
import com.example.SpringBoot_Twitter_Api_Project.entity.Retweet;
import com.example.SpringBoot_Twitter_Api_Project.service.RetweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final RetweetService retweetService;

    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    @PostMapping("/{tweetId}")
    public ResponseEntity<RetweetDTO> addRetweet( @PathVariable Long tweetId, Authentication authentication) {
        RetweetDTO retweet = retweetService.addRetweet(tweetId, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(retweet);
    }

    @DeleteMapping("/{retweetId}")
    public ResponseEntity<String> deleteRetweet(@PathVariable Long retweetId, Authentication authentication) {
        retweetService.deleteRetweet(retweetId, authentication.getName());
        return ResponseEntity.ok("Retweet successfully deleted.");
    }
}
