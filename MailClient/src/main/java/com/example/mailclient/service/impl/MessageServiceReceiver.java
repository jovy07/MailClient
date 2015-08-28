package com.example.mailclient.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mailclient.model.User;
import com.example.mailclient.service.UserService;
import com.sun.mail.pop3.POP3SSLStore;


@Service
public class MessageServiceReceiver {

	@Autowired
	private UserService userService;
	
	
	public HashMap<String, String> gmailServer(String host,final String username,final String password,String userLoginName) throws MessagingException, IOException{
	
		try{
			  Properties properties = new Properties();
		
			  properties.put("mail.imap.host", host);
		      properties.put("mail.imap.port", "993");
		     properties.put("mail.imap.starttls.enable", "true");
		      Session emailSession = Session.getDefaultInstance(properties);
		      
		      Store store = emailSession.getStore("imaps");
		      store.connect(host, username, password);
		    Folder [] f=store.getDefaultFolder().list();
		   
		      Folder emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		     
		      
		      Message message=emailFolder.getMessage(emailFolder.getMessageCount());
		      

		      Address [] in=message.getFrom();  
		      
		      HashMap<String,String> content=new HashMap<String, String>();
		      
		      String adresa="";
		      for (Address address : in) {
		    	  MimeUtility.decodeText(address.toString());
		    	  adresa+=address.toString();
		   
		      }
		     
		   
		      adresa+=":"+message.getSubject()+"....";
		      
		      User user=userService.findUser(userLoginName);
		      
	    	  for(HashMap<String,String> messages:user.inbox){
	    		  for(String key:messages.keySet()){
	    			  if(adresa.matches(key)){
	    				return null;
	    			  }
	    			
	    		  }
	    	  }
		   
		      
		      
		      if(message.isMimeType("text/plain")){
		    	 
		    	  content.put(adresa, message.getContent().toString());
		      }
		      else{

				Multipart multipart=(Multipart) message.getContent();
				
				 for (int j = 0; j < multipart.getCount(); j++) {

				        BodyPart bodyPart = multipart.getBodyPart(j); 
				   
				        	if(bodyPart.isMimeType("text/plain")){
				        		
				        		content.put(adresa, bodyPart.getContent().toString());
				        
				        	}
				     
				        	if(bodyPart.isMimeType("multipart/*")){
				        		Multipart mp=(Multipart) bodyPart.getContent();
				        		for(int i=0;i<mp.getCount();i++){
				        		
				        			content.put(adresa,(String)mp.getBodyPart(i).getContent());
				        		}
				        	}
				            
				 }
				
		      }
		      userService.filterMessages(userLoginName, username, adresa);
		      userService.saveToInbox(userLoginName, content);
		      return content;
		    
		}catch(NoSuchProviderException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public HashMap<String,String> liveServer(String host,String username,String password,String userLoginName) throws MessagingException, IOException{
		
		 Session session;
		 Store store;

		 String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		 
		 try{
			 Properties properties = new Properties();
			properties.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
			   
			    properties.setProperty("mail.pop3.port",  "995");
			    properties.setProperty("mail.pop3.socketFactory.port", "995");
			    
			    URLName url=new URLName("pop3", host, 995,"", username, password);
			    
			    session=Session.getInstance(properties, null);
			    store=new POP3SSLStore(session, url);
			    store.connect();
			/*  Properties properties = new Properties();
				
			  properties.put("mail.imap.host", host);
		      properties.put("mail.imap.port", "993");
		      properties.put("mail.imap.starttls.enable", "true");
		      Session emailSession = Session.getDefaultInstance(properties);
		      
		      Store store = emailSession.getStore("imaps");
		      store.connect(host, username, password);
			    Folder [] f=store.getDefaultFolder().list();*/
			  
			   
			  
			    Folder emailFolder=store.getFolder("INBOX");
			    emailFolder.open(Folder.READ_ONLY);
			      
			 
			    
			    Message message=emailFolder.getMessage(emailFolder.getMessageCount());
			   
			      Address [] in=message.getFrom();
			    
			      String adresa="";
			      for (Address address : in) {
			    	  adresa+=address.toString(); 
		            }
		      adresa+=":"+message.getSubject()+"....";
			
			      
			      User user=userService.findUser(userLoginName);
	      
			    	  for(HashMap<String,String> messages:user.inbox){
			    		  for(String key:messages.keySet()){
			    			  if(adresa.matches(key)){
			    				return null;
			    			  }
			    			
			    		  }
			    	  }
			    
			      
			      HashMap<String,String> content=new HashMap<String, String>();
			      
			      if(message.isMimeType("text/*") && !message.isMimeType("multipart/*")){
			    	  content.put(adresa, message.getContent().toString());
			      }
			      else{
			    	  Multipart multipart=(Multipart) message.getContent();
					
					 for (int j = 0; j < multipart.getCount(); j++) {

					        BodyPart bodyPart = multipart.getBodyPart(j); 
					   
					        	if(bodyPart.isMimeType("text/plain")){
					        		content.put(adresa, bodyPart.getContent().toString());
					        	}
					     
					        	if(bodyPart.isMimeType("multipart/*")){
					        		Multipart mp=(Multipart) bodyPart.getContent();
					        		for(int i=0;i<mp.getCount();i++){
					        			content.put(adresa,(String)mp.getBodyPart(i).getContent());
					        			
					        		}
					        	}
					            
					 }
			      }
			      userService.filterMessages(userLoginName, username, adresa);
					 userService.saveToInbox(userLoginName, content);
			      return content;
			    
			}catch(NoSuchProviderException e){
				e.printStackTrace();
			}
		 
		 return null;
			    
		 }

}

