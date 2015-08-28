package com.example.mailclient.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mailclient.model.User;
import com.example.mailclient.repository.UserRepository;

@Service
public class UserVerification {

	@Autowired
	UserRepository userRep;
	
	public boolean validate(User user){
		List<User> users=userRep.findAll();
		
		for(User u : users){
			
			if(u.getUsername().equals(user.getUsername()))
				return false;
		}
		return true;
		
	}
}
