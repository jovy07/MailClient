package com.example.mailclient.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import javax.mail.MessagingException;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mailclient.model.User;
import com.example.mailclient.repository.UserRepository;
import com.example.mailclient.service.UserService;


@Service
public class UserServiceImpl implements UserService {
	
	EntityManager em;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GetAllMailMessages getAllEmailMsgs;
	
	public void createUser(String firstName, String lastName, String username,
			String password, String birthdate,HashMap<String, String> mails,ArrayList<HashMap<String, String>>inbox,ArrayList<HashMap<String, String>>sent,HashMap<String, String> filteredMails, ArrayList<HashMap<String, String>> deleted) {
		// TODO Auto-generated method stub
		User user=new User(firstName, lastName, username, password, birthdate, mails,inbox,sent,filteredMails,deleted);
		userRepository.save(user);
	}

	public User findUser(String username) {
		// TODO Auto-generated method stub
		return userRepository.findOne(username);
	}

	public List<User> users() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public void addMail(String username, String mail,String password) {
		// TODO Auto-generated method stub
		User user=userRepository.findOne(username);
		user.addMail(mail,password);
		userRepository.saveAndFlush(user);
	}

	@Override
	public HashMap<String, String> getEmails(String username) {
		// TODO Auto-generated method stub
		HashMap<String, String> mailList=userRepository.findOne(username).mailList;

		
		return mailList;
	}

	@Override
	public void saveToInbox(String userLoginName, HashMap<String, String> message) {
		// TODO Auto-generated method stub
		User user=userRepository.findOne(userLoginName);
		user.inbox.add(message);
		
		userRepository.saveAndFlush(user);
	}


	@Override
	public void refreshServer(String username) throws MessagingException, IOException {
		// TODO Auto-generated method stub
		User user=userRepository.findOne(username);
		
		getAllEmailMsgs.getAllMessages(user.mailList, username);
		
	}

	@Override
	public void saveToSent(String userLoginName, HashMap<String, String> message) {
		// TODO Auto-generated method stub
		User user=userRepository.findOne(userLoginName);
		user.sent.add(message);
		
		userRepository.saveAndFlush(user);
	}

	@Override
	public void filterMessages(String username, String host, String inboxMessage) {
		// TODO Auto-generated method stub
		User user=userRepository.findOne(username);
		user.filteredMails.put(host, inboxMessage);
		
		userRepository.saveAndFlush(user);
	}
	
	
	@Override
	public void deleteMessage(String username, String message) {
		// TODO Auto-generated method stub
		User user=userRepository.findOne(username);
		System.out.println("MESSAGE");
		System.out.println(message);
		Iterator<HashMap<String, String>> iter=user.inbox.iterator();
		ArrayList<HashMap<String, String>> newInbox=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> newSent=new ArrayList<HashMap<String,String>>();
		
		while(iter.hasNext()){
			HashMap<String,String> messageMap=iter.next();
			if(messageMap.containsKey(message))
			{
				user.deleted.add(messageMap);
				iter.remove();
								
			}else{
				newInbox.add(messageMap);
			}
		}
		user.inbox.clear();
		user.inbox=newInbox;
	
		
		
		Iterator<HashMap<String, String>> iterSent=user.sent.iterator();
		while(iterSent.hasNext()){
			HashMap<String,String> messageMap=iterSent.next();
			if(messageMap.containsKey(message)){
				iterSent.remove();
			}else{
				newSent.add(messageMap);
			}
	
		}
		user.sent.clear();
		user.sent=newSent;
		userRepository.saveAndFlush(user);
	}


}
