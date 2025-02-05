package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.Like;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.LikeRepository;
import com.example.SpringBoot_Twitter_Api_Project.repository.TweetRepository;
import com.example.SpringBoot_Twitter_Api_Project.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, TweetRepository tweetRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public Like addlike(Long userId, Long tweetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TwitterException("User not found", HttpStatus.NOT_FOUND));

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found", HttpStatus.NOT_FOUND));

        Optional<Like> existingLike = likeRepository.findByUserAndTweet(user, tweet);
        if (existingLike.isPresent()) {
            throw new TwitterException("User already liked this tweet", HttpStatus.BAD_REQUEST);
        }

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        like.setIsLiked(true);

        return likeRepository.save(like);
    }

    public void removeLike(Long userId, Long tweetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TwitterException("User not found", HttpStatus.NOT_FOUND));

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found", HttpStatus.NOT_FOUND));

        Like like = likeRepository.findByUserAndTweet(user, tweet)
                .orElseThrow(() -> new TwitterException("Like not found", HttpStatus.NOT_FOUND));

        likeRepository.delete(like);
    }
}
