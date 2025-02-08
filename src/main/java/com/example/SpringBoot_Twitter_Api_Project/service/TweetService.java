package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.TweetRepository;
import com.example.SpringBoot_Twitter_Api_Project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    private void validateTweetOwner(Tweet tweet, String username) {
        if (!tweet.getUser().getUsername().equals(username)) {
            throw new TwitterException("You are not authorized to modify this tweet.", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public Tweet createTweet(String content, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user);

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
