package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.Retweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.RetweetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

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

    public Retweet addRetweet(Long tweetId, Long userId) {
        Tweet tweet = tweetService.findById(tweetId);
        User user = userService.findUserById(userId);

        if (tweet.getUser().getId().equals(userId)) {
            throw new TwitterException("You cannot retweet your own tweet.", HttpStatus.BAD_REQUEST);
        }

        Optional<Retweet> existingRetweet = retweetRepository.findByUserAndTweet(user, tweet);
        if (existingRetweet.isPresent()) {
            throw new TwitterException("You have already retweeted this tweet.", HttpStatus.BAD_REQUEST);
        }

        Retweet retweet = new Retweet(user, tweet);
        return retweetRepository.save(retweet);
    }

    public void deleteRetweet(Long retweetId) {
        Retweet retweet = retweetRepository.findById(retweetId)
                .orElseThrow(() -> new TwitterException("Retweet not found with id: " + retweetId, HttpStatus.NOT_FOUND));
        retweetRepository.delete(retweet);
    }

    public Retweet findById(Long id) {
        return retweetRepository.findById(id)
                .orElseThrow(() -> new TwitterException("Retweet not found with id: " + id, HttpStatus.NOT_FOUND));
    }
}
