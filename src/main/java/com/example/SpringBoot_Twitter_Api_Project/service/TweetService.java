package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.dto.*;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TweeterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.TweetRepository;
import com.example.SpringBoot_Twitter_Api_Project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<?> createTweet(String content, String username) {
        // Kullanıcı kontrolü
        User user = findByUserName(username);

        //Tweet oluştur
        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user);

        //Tweeti kaydet
        tweetRepository.save(tweet);

        return ResponseEntity.ok("Tweet created\n" + tweet.getContent());
    }

    public ResponseEntity<?> updateTweet(Long tweetId, String newContent, String username) {
        // Kullanıcı kontrolü
        User user = findByUserName(username);

        // Tweet'i bul
        Tweet tweet = findById(tweetId);

        // Tweet sahibi kontrolü
        if (!tweet.getUser().getUsername().equals(username)) {
            throw new TweeterException("You are not authorization to update this tweet. You can only update your own tweets.",
                    HttpStatus.FORBIDDEN);
        }

        //Tweeti güncelle
        tweet.setContent(newContent);

        //Güncellenmiş tweeti veri tabanına kaydet
        tweetRepository.save(tweet);
        
        return ResponseEntity.ok("Tweet updated\n" + tweet.getContent());
    }

    public ResponseEntity<?> deleteTweet(Long tweetId, String username) {
        // Kullanıcı kontrolü
        User user = findByUserName(username);

        // Tweet'i bul
        Tweet tweet = findById(tweetId);

        // Tweet sahibi kontrolü
        if (!tweet.getUser().getUsername().equals(username)) {
            throw new TweeterException("You are not authorization to delete this tweet. You can only delete your own tweets.",
                    HttpStatus.FORBIDDEN);
        }

        //Tweeti sil
        tweetRepository.delete(tweet);

        return ResponseEntity.ok("Tweet deleted\n" + tweet.getContent());
    }

    public TweetDTO getByIdTweet(Long tweetId) {
        Tweet tweet = findById(tweetId);
        return convertToDTO(tweet);
    }

    public List<TweetDTO> getTweetsByUserId(Long userId) {
        // Önce kullanıcının var olup olmadığını kontrol et
        userRepository.findById(userId)
                .orElseThrow(() -> new TweeterException("User not found: " + userId, HttpStatus.NOT_FOUND));

        // Kullanıcının tweetlerini getir
        List<Tweet> tweets = tweetRepository.findByUserId(userId);

        // Tweet listesini DTO listesine dönüştür
        return tweets.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new TweeterException("You are not authorization for this action", HttpStatus.NOT_FOUND));
    }

    public Tweet findById(Long tweetId) {
        return tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweeterException("Tweet not found with id: " + tweetId, HttpStatus.NOT_FOUND));

    }

    private TweetDTO convertToDTO(Tweet tweet) {
        TweetDTO tweetDTO = new TweetDTO();
        tweetDTO.setId(tweet.getId());
        tweetDTO.setContent(tweet.getContent());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(tweet.getUser().getId());
        userDTO.setUsername(tweet.getUser().getUsername());
        tweetDTO.setUser(userDTO);

        List<CommentDTO> commentDTOs = tweet.getComments().stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    commentDTO.setId(comment.getId());
                    commentDTO.setContent(comment.getContent());
                    commentDTO.setUser(new UserDTO(
                            comment.getUser().getId(),
                            comment.getUser().getUsername()
                    ));
                    return commentDTO;
                })
                .toList();
        tweetDTO.setComments(commentDTOs);

        List<LikeDTO> likeDTOs = tweet.getLikes().stream()
                .map(like -> {
                    LikeDTO likeDTO = new LikeDTO();
                    likeDTO.setId(like.getId());
                    likeDTO.setUser(new UserDTO(
                            like.getUser().getId(),
                            like.getUser().getUsername()
                    ));
                    return likeDTO;
                })
                .toList();
        tweetDTO.setLikes(likeDTOs);

        List<RetweetDTO> retweetDTOs = tweet.getRetweets().stream()
                .map(retweet -> {
                    RetweetDTO retweetDTO = new RetweetDTO();
                    retweetDTO.setId(retweet.getId());
                    retweetDTO.setUser(new UserDTO(
                            retweet.getUser().getId(),
                            retweet.getUser().getUsername()
                    ));
                    return retweetDTO;
                })
                .toList();
        tweetDTO.setRetweets(retweetDTOs);

        return tweetDTO;
    }



}
