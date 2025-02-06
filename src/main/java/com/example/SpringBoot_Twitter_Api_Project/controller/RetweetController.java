package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Retweet;
import com.example.SpringBoot_Twitter_Api_Project.service.RetweetService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final RetweetService retweetService;

    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    @PostMapping("/{tweetId}/{userId}")
    public Retweet addRetweet(@PathVariable Long tweetId, @PathVariable Long userId) {
        return retweetService.addRetweet(tweetId, userId);
    }

    @DeleteMapping("/{id}")
    public String deleteRetweet(@PathVariable Long id, Authentication authentication) {
        retweetService.deleteRetweet(id, authentication.getName());
        return "Retweet deleted successfully.";
    }
}
