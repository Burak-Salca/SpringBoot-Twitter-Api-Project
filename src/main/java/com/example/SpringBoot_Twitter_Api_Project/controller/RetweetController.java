package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Retweet;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
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

    @PostMapping
    public ResponseEntity<Retweet> addRetweet(@PathVariable Long tweetId, @PathVariable Long userId) {
        Retweet savedRetweet = retweetService.addRetweet(tweetId, userId);
        return new ResponseEntity<>(savedRetweet, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRetweet(@PathVariable Long id, Authentication authentication) {
        String currentUsername = authentication.getName();
        Retweet retweet = retweetService.findById(id);

        // Retweet sahibini kontrol et
        if (!retweet.getUser().getUsername().equals(currentUsername)) {
            throw new TwitterException("You are not authorized to delete this retweet.", HttpStatus.FORBIDDEN);
        }

        // Silme işlemi
        retweetService.deleteRetweet(id);
        return new ResponseEntity<>("Retweet deleted successfully.", HttpStatus.OK);
    }
}
