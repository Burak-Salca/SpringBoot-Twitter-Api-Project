package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.dto.CommentDTO;
import com.example.SpringBoot_Twitter_Api_Project.dto.UserDTO;
import com.example.SpringBoot_Twitter_Api_Project.entity.Comment;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TweeterException;
import com.example.SpringBoot_Twitter_Api_Project.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetService tweetService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, TweetService tweetService, UserService userService) {
        this.commentRepository = commentRepository;
        this.tweetService = tweetService;
        this.userService = userService;
    }

    public CommentDTO createComment(Long tweetId, String username, String content) {
        // Tweet'in var olup olmadığını kontrol et
        Tweet tweet = tweetService.findById(tweetId);
        
        // Kullanıcının var olup olmadığını kontrol et
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new TweeterException("User not found.", HttpStatus.NOT_FOUND));

        // Yeni comment oluştur
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setTweet(tweet);
        
        // Comment'i kaydet
        Comment savedComment = commentRepository.save(comment);
        
        // DTO'ya dönüştür ve geri dön
        return convertToDTO(savedComment);
    }

    public CommentDTO updateComment(Long commentId, String newContent, String username) {
        // Yorumu bul
        Comment comment = findById(commentId);
        
        // Yorum sahibi kontrolü
        if (!comment.getUser().getUsername().equals(username)) {
            throw new TweeterException("You are not authorized to modify this comment.", HttpStatus.FORBIDDEN);
        }
        
        // Yorumu güncelle
        comment.setContent(newContent);
        Comment updatedComment = commentRepository.save(comment);
        
        // DTO'ya dönüştür ve geri dön
        return convertToDTO(updatedComment);
    }

    public void deleteComment(Long commentId, String username) {
        // Yorumu bul
        Comment comment = findById(commentId);
        
        // Tweet sahibini ve yorum sahibini kontrol et
        boolean isCommentOwner = comment.getUser().getUsername().equals(username);
        boolean isTweetOwner = comment.getTweet().getUser().getUsername().equals(username);
        
        // Eğer ne yorum sahibi ne de tweet sahibi değilse hata fırlat
        if (!isCommentOwner && !isTweetOwner) {
            throw new TweeterException("You are not authorized to delete this comment. Only comment owner or tweet owner can delete.",
                HttpStatus.FORBIDDEN);
        }
        
        // Yorumu sil
        commentRepository.delete(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new TweeterException("Comment not found: " + id, HttpStatus.NOT_FOUND));
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(comment.getUser().getId());
        userDTO.setUsername(comment.getUser().getUsername());
        dto.setUser(userDTO);

        return dto;
    }
}
