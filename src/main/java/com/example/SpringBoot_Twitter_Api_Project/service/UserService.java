package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new TwitterException("User not found with id: " + userId, HttpStatus.NOT_FOUND));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new TwitterException("User not found with username: " + username, HttpStatus.NOT_FOUND));
    }
}
