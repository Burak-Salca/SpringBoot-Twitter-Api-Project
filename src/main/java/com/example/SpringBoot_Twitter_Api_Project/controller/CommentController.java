package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.dto.CommentDTO;
import com.example.SpringBoot_Twitter_Api_Project.dto.CommentRequest;
import com.example.SpringBoot_Twitter_Api_Project.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{tweetId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long tweetId, @Valid @RequestBody CommentRequest commentRequest, Authentication authentication) {
        CommentDTO createdComment = commentService.createComment(tweetId, authentication.getName(), commentRequest.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequest commentRequest, Authentication authentication) {
        CommentDTO updatedComment = commentService.updateComment(commentId, commentRequest.getContent(), authentication.getName());
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment( @PathVariable Long commentId,  Authentication authentication) {
        commentService.deleteComment(commentId, authentication.getName());
        return ResponseEntity.ok("Comment successfully deleted.");
    }
}
