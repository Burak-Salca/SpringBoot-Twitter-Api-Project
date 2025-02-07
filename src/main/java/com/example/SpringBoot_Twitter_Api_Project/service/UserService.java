package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.dto.RegisterRequest;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.SpringBoot_Twitter_Api_Project.dto.LoginRequest;
import com.example.SpringBoot_Twitter_Api_Project.dto.LoginResponse;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new TwitterException("User not found with id: " + userId, HttpStatus.NOT_FOUND));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new TwitterException("User not found with this username: " + username, HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                .badRequest()
                .body("Bu kullanıcı adı zaten kullanılıyor!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        userRepository.save(user);
        
        return ResponseEntity.ok("Kullanıcı başarıyla kaydedildi!");
    }

    public ResponseEntity<?> login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity
                .badRequest()
                .body("Geçersiz şifre!");
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token, user.getUsername()));
    }
}
