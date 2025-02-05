package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Comment;
import com.example.SpringBoot_Twitter_Api_Project.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestParam Long userId, @RequestParam String newContent) {
        Comment updatedComment = commentService.updateComment(id, userId, newContent);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return new ResponseEntity<>("Comment successfully deleted.", HttpStatus.OK);
    }
}
