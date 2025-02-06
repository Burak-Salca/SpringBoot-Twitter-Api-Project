package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
import com.example.SpringBoot_Twitter_Api_Project.service.TweetService;

import com.example.SpringBoot_Twitter_Api_Project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final TweetService tweetService;
    private final UserService userService;

    public TweetController(TweetService tweetService, UserService userService ) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Tweet> createTweet(@RequestBody Tweet tweetRequest, Authentication authentication) {
        // Authentication'dan mevcut kullanıcı adını al
        String currentUsername = authentication.getName();

        // Kullanıcıyı bul
        User user = userService.findUserByUsername(currentUsername);

        if (user == null) {
            throw new TwitterException("User not found.", HttpStatus.NOT_FOUND);
        }

        // Tweet oluştur
        Tweet savedTweet = tweetService.createTweet(user.getId(), tweetRequest.getContent());
        return new ResponseEntity<>(savedTweet, HttpStatus.CREATED);
    }

    @GetMapping("/findById")
    public ResponseEntity<Tweet> findById(@RequestParam Long id) {
        Tweet tweet = tweetService.findById(id);
        return new ResponseEntity<>(tweet, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable Long id, @RequestBody String newTweet, Authentication authentication) {
        String currentUsername = authentication.getName();

        // Tweet'i bulma
        Tweet tweet = tweetService.findById(id);

        if (tweet == null) {
            throw new TwitterException("Tweet not found.", HttpStatus.NOT_FOUND);
        }

        // Tweet sahibini kontrol et
        if (!tweet.getUser().getUsername().equals(currentUsername)) {
            throw new TwitterException("You are not authorized to update this tweet.", HttpStatus.FORBIDDEN);
        }

        // Tweet içeriğini güncelleme
        tweetService.updateTweet(id, newTweet);
        return new ResponseEntity<>(tweet, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTweet(@PathVariable Long id, Authentication authentication) {
        String currentUsername = authentication.getName();

        // Tweet'i bulma
        Tweet tweet = tweetService.findById(id);

        if (tweet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tweet not found.");
        }

        // Tweet sahibini kontrol et
        if (!tweet.getUser().getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this tweet.");
        }

        // Silme işlemi
        tweetService.deleteTweet(id);
        return ResponseEntity.ok("Tweet deleted successfully.");
    }
}
