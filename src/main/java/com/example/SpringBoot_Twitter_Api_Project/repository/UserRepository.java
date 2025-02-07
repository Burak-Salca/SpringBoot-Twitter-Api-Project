package com.example.SpringBoot_Twitter_Api_Project.repository;

import com.example.SpringBoot_Twitter_Api_Project.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(@NotBlank(message = "Username is required") String username);
}
