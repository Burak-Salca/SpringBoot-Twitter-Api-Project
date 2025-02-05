package com.example.SpringBoot_Twitter_Api_Project.service;

import com.example.SpringBoot_Twitter_Api_Project.entity.Comment;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import com.example.SpringBoot_Twitter_Api_Project.exception.TwitterException;
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

    public Comment addComment(Long tweetId, Long userId, Comment commentRequest) {
        Tweet tweet = tweetService.findById(tweetId);
        User user = userService.findUserById(userId);

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setUser(user);
        comment.setTweet(tweet);

        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TwitterException("Comment not found with id: " + commentId, HttpStatus.NOT_FOUND));

        if (!comment.getUser().getId().equals(userId) && !comment.getTweet().getUser().getId().equals(userId)) {
            throw new TwitterException("You are not authorized to delete this comment.", HttpStatus.FORBIDDEN);
        }
        commentRepository.delete(comment);
    }

    public Comment updateComment(Long commentId, Long userId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TwitterException("Comment not found with id: " + commentId, HttpStatus.NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new TwitterException("You are not authorized to update this comment.", HttpStatus.FORBIDDEN);
        }

        comment.setContent(newContent);
        return commentRepository.save(comment);
    }
}
