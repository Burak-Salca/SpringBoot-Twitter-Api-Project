package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Comment;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
import com.example.SpringBoot_Twitter_Api_Project.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/")
    public ResponseEntity<Comment> addComment(@RequestParam Long tweetId, @RequestParam Long userId, @RequestBody Comment commentRequest) {
        Comment savedComment = commentService.addComment(tweetId, userId, commentRequest);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestParam String newContent, Authentication authentication) {
        String currentUsername = authentication.getName();
        Comment comment = commentService.findById(id);

        // Yorum sahibi ile kullanıcı adı eşleşiyor mu kontrol et
        if (!comment.getUser().getUsername().equals(currentUsername)) {
            throw new TwitterException("You are not authorized to update this comment.", HttpStatus.FORBIDDEN);
        }

        // Yorum güncelleme işlemi
        Comment updatedComment = commentService.updateComment(id, newContent);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, Authentication authentication) {
        String currentUsername = authentication.getName();
        Comment comment = commentService.findById(id);

        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found.");
        }

        // Yorum sahibini kontrol et
        if (!comment.getUser().getUsername().equals(currentUsername)) {
            throw new TwitterException("You are not authorized to delete this comment.", HttpStatus.FORBIDDEN);
        }

        // Yorum silme işlemi
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully.");
    }
}
