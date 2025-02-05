package com.example.SpringBoot_Twitter_Api_Project.repository;

import com.example.SpringBoot_Twitter_Api_Project.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByUserId(Long userId);
}