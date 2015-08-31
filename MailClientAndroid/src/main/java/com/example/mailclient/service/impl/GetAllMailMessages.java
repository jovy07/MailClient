package com.example.mailclient.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.mail.handlers.message_rfc822;


@Service
public class GetAllMailMessages {

	@Autowired
	MessageServiceReceiver messageServiceReceiver;
	
	@Autowired
	HostServerElection hostServerElection;
	
	
	public ArrayList<HashMap<String,String>> getAllMessages(HashMap<String, String>mailList,String userLoginName) throws MessagingException, IOException{
		
		ArrayList<HashMap<String,String>> messages = new ArrayList<HashMap<String,String>>();
		
			ArrayList<String>	mails = new ArrayList<String>();
		
		for(String key: mailList.keySet()){
			mails.add(key);
		}
		
		for(String mail:mailList.keySet()){
			String password=mailList.get(mail);
			
			String host="";
			if(hostServerElection.mailSelection(mail).equals("imap.gmail.com")){
				host="imap.gmail.com";
				if(messageServiceReceiver.gmailServer(host,mail, password, userLoginName)!=null){
				messages.add(messageServiceReceiver.gmailServer(host, mail, password,userLoginName));
				}
			
			}
			
			if(hostServerElection.mailSelection(mail).equals("pop3.live.com")){
				host="pop3.live.com";
				if(messageServiceReceiver.liveServer(host, mail, password, userLoginName)!=null){
					messages.add(messageServiceReceiver.liveServer(host, mail, password,userLoginName));
				}
			
			}
			
			
		
		}
		
		return messages;
		
	}
	
}
