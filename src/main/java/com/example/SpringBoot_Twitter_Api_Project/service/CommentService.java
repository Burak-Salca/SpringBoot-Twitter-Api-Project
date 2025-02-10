package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.Comment;
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

    private void validateCommentOwner(Comment comment, String username) {
        if (!comment.getUser().getUsername().equals(username)) {
            throw new TweeterException("You are not authorized to modify this comment.", HttpStatus.FORBIDDEN);
        }
    }

   /* public Comment addComment(Long tweetId, String username, Comment) {
        Tweet tweet = tweetService.findById(tweetId);
        User user = userService.loadUserByUsername(username);
        if (user == null) {
            throw new TweeterException("User not found.", HttpStatus.NOT_FOUND);
        }

        Comment comment = new Comment(commentRequest.getContent(), user, tweet);
        return commentRepository.save(comment);
    }*/

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new TweeterException("Comment not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    public Comment updateComment(Long commentId, String newContent, String username) {
        Comment comment = findById(commentId);
        validateCommentOwner(comment, username);
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = findById(commentId);
        validateCommentOwner(comment, username);
        commentRepository.delete(comment);
    }
}
