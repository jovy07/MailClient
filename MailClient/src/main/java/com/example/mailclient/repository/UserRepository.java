package com.example.mailclient.repository;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailclient.model.User;

public interface UserRepository extends JpaRepository<User, String>{

	
}
