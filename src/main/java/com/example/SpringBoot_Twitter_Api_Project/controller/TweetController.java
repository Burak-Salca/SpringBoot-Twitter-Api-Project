package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.service.TweetService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public Tweet createTweet(@RequestBody Tweet tweetRequest, Authentication authentication) {
        return tweetService.createTweet(authentication.getName(), tweetRequest.getContent());
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
