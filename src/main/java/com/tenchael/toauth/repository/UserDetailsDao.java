package com.tenchael.toauth.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tenchael.toauth.domain.UserDetails;

public interface UserDetailsDao extends JpaRepository<UserDetails, Integer> {

	List<UserDetails> findAll(Specification<UserDetails> spec);

}
