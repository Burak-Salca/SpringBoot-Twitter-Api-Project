package com.example.SpringBoot_Twitter_Api_Project.repository;

import com.example.SpringBoot_Twitter_Api_Project.entity.Like;
import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndTweetAndIsLikedTrue(User user, Tweet tweet);

    Optional<Like> findByUserAndTweet(User user, Tweet tweet);
}
