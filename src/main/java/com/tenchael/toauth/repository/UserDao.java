package com.tenchael.toauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenchael.toauth.domain.User;

public interface UserDao extends JpaRepository<User, Integer> {

	User findByUsername(String username);

}
