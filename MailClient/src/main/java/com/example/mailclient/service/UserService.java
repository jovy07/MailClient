package com.example.mailclient.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import com.example.mailclient.model.User;

public interface UserService {

	public void createUser(String firstName, String lastName, String username,
			String password, String birthdate, HashMap<String,String>mails,ArrayList<HashMap<String, String>> inbox,ArrayList<HashMap<String, String>>sent,HashMap<String, String> filteredMails, ArrayList<HashMap<String, String>> deleted);
	
	public User findUser(String username);
	
	public List<User> users();
	
	public void addMail(String username,String mail,String password);
	
	public HashMap<String,String> getEmails(String username);
	
	
	public void saveToInbox(String userLoginName,HashMap<String,String> message);

	public void refreshServer(String username) throws MessagingException, IOException;
	
	public void saveToSent(String userLoginName,HashMap<String,String>message);
	
	public void filterMessages(String username,String host,String inboxMessage);
	
	public void deleteMessage(String username,String message);
}
