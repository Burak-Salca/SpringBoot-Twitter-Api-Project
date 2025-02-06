package com.example.SpringBoot_Twitter_Api_Project.controller;

import com.example.SpringBoot_Twitter_Api_Project.entity.Comment;
import com.example.SpringBoot_Twitter_Api_Project.service.CommentService;
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
    public Comment addComment(@RequestParam Long tweetId, Authentication authentication, @RequestBody Comment commentRequest) {
        return commentService.addComment(tweetId, authentication.getName(), commentRequest);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody String newContent, Authentication authentication) {
        return commentService.updateComment(id, newContent, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id, Authentication authentication) {
        commentService.deleteComment(id, authentication.getName());
        return "Comment deleted successfully.";
    }
}
