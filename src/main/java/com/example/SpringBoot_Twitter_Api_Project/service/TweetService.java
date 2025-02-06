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

    private void validateTweetOwner(Tweet tweet, String username) {
        if (!tweet.getUser().getUsername().equals(username)) {
            throw new TwitterException("You are not authorized to modify this tweet.", HttpStatus.FORBIDDEN);
        }
    }

    public Tweet createTweet(String username, String content) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new TwitterException("User not found.", HttpStatus.NOT_FOUND);
        }
        Tweet tweet = new Tweet(content, user);
        return tweetRepository.save(tweet);
    }

    public Tweet findById(Long tweetId) {
        return tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found with id: " + tweetId, HttpStatus.NOT_FOUND));
    }

    public Tweet updateTweet(Long tweetId, String newContent, String username) {
        Tweet tweet = findById(tweetId);
        validateTweetOwner(tweet, username);
        tweet.setContent(newContent);
        return tweetRepository.save(tweet);
    }

    public void deleteTweet(Long tweetId, String username) {
        Tweet tweet = findById(tweetId);
        validateTweetOwner(tweet, username);
        tweetRepository.delete(tweet);
    }
}
