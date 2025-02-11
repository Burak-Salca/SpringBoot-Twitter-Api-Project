package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.dto.LikeDTO;
import com.example.SpringBoot_Twitter_Api_Project.dto.UserDTO;
import com.example.SpringBoot_Twitter_Api_Project.entity.Like;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TweeterException;
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
    private final TweetService tweetService;
    private final UserService userService;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, TweetRepository tweetRepository, TweetService tweetService, UserService userService) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
        this.tweetService = tweetService;
        this.userService = userService;
    }

    public LikeDTO addLike(Long tweetId, String username) {
        // Tweet'in var olup olmadığını kontrol et
        Tweet tweet = tweetService.findById(tweetId);
        
        // Kullanıcının var olup olmadığını kontrol et
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new TweeterException("User not found.", HttpStatus.NOT_FOUND));
        
        // Önce mevcut like kaydını kontrol et
        Optional<Like> existingLike = likeRepository.findByUserAndTweet(user, tweet);
        
        if (existingLike.isPresent()) {
            Like like = existingLike.get();
            if (like.getIsLiked()) {
                throw new TweeterException("You have already liked this tweet.", HttpStatus.BAD_REQUEST);
            } else {
                // Daha önce dislike yapılmış, tekrar like yapılabilir
                like.setIsLiked(true);
                Like updatedLike = likeRepository.save(like);
                return convertToDTO(updatedLike);
            }
        }

        // Yeni like oluştur
        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        like.setIsLiked(true);
        
        // Like'ı kaydet
        Like savedLike = likeRepository.save(like);
        
        // DTO'ya dönüştür ve geri dön
        return convertToDTO(savedLike);
    }

    public LikeDTO removeLike(Long tweetId, String username) {
        // Tweet'in var olup olmadığını kontrol et
        Tweet tweet = tweetService.findById(tweetId);

        // Kullanıcının var olup olmadığını kontrol et
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new TweeterException("User not found.", HttpStatus.NOT_FOUND));
        
        // Kullanıcının tweet'i beğenip beğenmediğini kontrol et
        Like like = likeRepository.findByUserAndTweet(user, tweet)
                .orElseThrow(() -> new TweeterException("You haven't liked this tweet yet.", HttpStatus.BAD_REQUEST));
        
        // Like'ın aktif olup olmadığını kontrol et
        if (!like.getIsLiked()) {
            throw new TweeterException("You have already unliked this tweet.", HttpStatus.BAD_REQUEST);
        }
        
        // Like'ın sahibi olup olmadığını kontrol et
        if (!like.getUser().getUsername().equals(username)) {
            throw new TweeterException("You are not authorized to remove this like.", HttpStatus.FORBIDDEN);
        }
        
        // Like'ı false yap
        like.setIsLiked(false);
        Like updatedLike = likeRepository.save(like);
        
        // DTO'ya dönüştür ve geri dön
        return convertToDTO(updatedLike);
    }

    private LikeDTO convertToDTO(Like like) {
        LikeDTO dto = new LikeDTO();
        dto.setId(like.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(like.getUser().getId());
        userDTO.setUsername(like.getUser().getUsername());
        dto.setUser(userDTO);

        dto.setTweetId(like.getTweet().getId());
        dto.setIsLiked(like.getIsLiked());

        return dto;
    }
}
