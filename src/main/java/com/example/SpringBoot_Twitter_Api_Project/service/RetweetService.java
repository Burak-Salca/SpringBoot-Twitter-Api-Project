package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.Retweet;
import com.example.SpringBoot_Twitter_Api_Project.exception.TweeterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.RetweetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetService tweetService;
    private final UserService userService;

    public RetweetService(RetweetRepository retweetRepository, TweetService tweetService, UserService userService) {
        this.retweetRepository = retweetRepository;
        this.tweetService = tweetService;
        this.userService = userService;
    }

    /*public Retweet addRetweet(Long tweetId, Long userId) {
        Tweet tweet = tweetService.findById(tweetId);
        User user = userService.findUserById(userId);

        if (tweet.getUser().getId().equals(userId)) {
            throw new TweeterException("You cannot retweet your own tweet.", HttpStatus.BAD_REQUEST);
        }

        Optional<Retweet> existingRetweet = retweetRepository.findByUserAndTweet(user, tweet);
        if (existingRetweet.isPresent()) {
            throw new TweeterException("You have already retweeted this tweet.", HttpStatus.BAD_REQUEST);
        }

        Retweet retweet = new Retweet(user, tweet);
        return retweetRepository.save(retweet);
    }*/

    public void deleteRetweet(Long retweetId, String username) {
        Retweet retweet = findById(retweetId);
        if (!retweet.getUser().getUsername().equals(username)) {
            throw new TweeterException("You are not authorized to delete this retweet.", HttpStatus.FORBIDDEN);
        }
        retweetRepository.delete(retweet);
    }

    public Retweet findById(Long id) {
        return retweetRepository.findById(id)
                .orElseThrow(() -> new TweeterException("Retweet not found with id: " + id, HttpStatus.NOT_FOUND));
    }
}
