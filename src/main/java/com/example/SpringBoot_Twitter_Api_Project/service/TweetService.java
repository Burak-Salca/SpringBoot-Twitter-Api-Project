package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.TweetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;

    public TweetService(TweetRepository tweetRepository, UserService userService) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
    }

    public Tweet createTweet(Long userId, String content) {
        User user = userService.findUserById(userId);
        Tweet tweet = new Tweet(content,user);
        return tweetRepository.save(tweet);
    }

    public Tweet findById(Long tweetId) {
        return tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found with id: " + tweetId, HttpStatus.NOT_FOUND));
    }

    public Tweet updateTweet(Long tweetId, String newContent) {
        Tweet tweet = findById(tweetId);
        tweet.setContent(newContent);
        return tweetRepository.save(tweet);
    }

    public void deleteTweet(Long tweetId) {
        Tweet tweet = findById(tweetId);
        tweetRepository.delete(tweet);
    }
}
