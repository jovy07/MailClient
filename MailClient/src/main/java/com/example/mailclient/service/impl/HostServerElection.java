package com.example.mailclient.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HostServerElection {

	
	
	public String mailSelection(String mail){
		
		String host="";
		
		if(mail.contains("gmail"))
			host="imap.gmail.com";
		
		if(mail.contains("live") || mail.contains("hotmail"))
			host="pop3.live.com";
		
		return host;
	}
}
