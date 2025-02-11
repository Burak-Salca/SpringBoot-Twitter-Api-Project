package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.Retweet;
import com.example.SpringBoot_Twitter_Api_Project.exception.TweeterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.RetweetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.SpringBoot_Twitter_Api_Project.dto.RetweetDTO;
import com.example.SpringBoot_Twitter_Api_Project.dto.UserDTO;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;

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

    public RetweetDTO addRetweet(Long tweetId, String username) {
        // Tweet'in var olup olmadığını kontrol et
        Tweet tweet = tweetService.findById(tweetId);
        
        // Kullanıcının var olup olmadığını kontrol et
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new TweeterException("User not found.", HttpStatus.NOT_FOUND));

        // Kullanıcının kendi tweet'ini retweet etmesini engelle
        if (tweet.getUser().getUsername().equals(username)) {
            throw new TweeterException("You cannot retweet your own tweet.", HttpStatus.BAD_REQUEST);
        }

        // Daha önce retweet yapılıp yapılmadığını kontrol et
        if (retweetRepository.existsByUserAndTweet(user, tweet)) {
            throw new TweeterException("You have already retweeted this tweet.", HttpStatus.BAD_REQUEST);
        }

        // Yeni retweet oluştur
        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);
        
        // Retweet'i kaydet
        Retweet savedRetweet = retweetRepository.save(retweet);
        
        // DTO'ya dönüştür ve geri dön
        return convertToDTO(savedRetweet);
    }

    public void deleteRetweet(Long retweetId, String username) {
        Retweet retweet = findById(retweetId);
        
        // Retweet'in sahibi olup olmadığını kontrol et
        if (!retweet.getUser().getUsername().equals(username)) {
            throw new TweeterException("You are not authorized to delete this retweet.", HttpStatus.FORBIDDEN);
        }
        
        retweetRepository.delete(retweet);
    }

    private Retweet findById(Long id) {
        return retweetRepository.findById(id)
                .orElseThrow(() -> new TweeterException("Retweet not found: " + id, HttpStatus.NOT_FOUND));
    }

    private RetweetDTO convertToDTO(Retweet retweet) {
        RetweetDTO dto = new RetweetDTO();
        dto.setId(retweet.getId());
        
        UserDTO userDTO = new UserDTO();
        userDTO.setId(retweet.getUser().getId());
        userDTO.setUsername(retweet.getUser().getUsername());
        dto.setUser(userDTO);
        
        dto.setTweetId(retweet.getTweet().getId());
        
        return dto;
    }
}
