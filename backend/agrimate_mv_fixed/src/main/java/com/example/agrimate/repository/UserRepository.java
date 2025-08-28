package com.example.agrimate.repository;

import com.example.agrimate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username); // Add this
    boolean existsByEmail(String email);       // Add this
}
