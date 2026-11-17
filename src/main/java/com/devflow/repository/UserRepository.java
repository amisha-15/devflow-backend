package com.devflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devflow.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {
	
	 // Find a user by email
	Optional<User> findByEmail(String email);
	
	// Check whether an email already exists
	boolean existsByEmail(String email);

	
}
