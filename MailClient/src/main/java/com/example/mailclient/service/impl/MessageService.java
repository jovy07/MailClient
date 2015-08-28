package com.example.mailclient.service.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;


@Service
public class MessageService {


	public void sendGmail(String msgReceiver,String msgSender,String password,String subject,String messageBody){
		Properties props = new Properties();
		final String sender=msgSender;
		final String userPassword=password;
		final String finalSubject=subject;
		final String finalMessageBody=messageBody;
	
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.debug", "false");
		props.put("mail.smtp.port", "587");
		
	
	
		
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(sender,userPassword);
					}
				});
		
		try {
			 
			Message message = new MimeMessage(session);
			  message.setHeader("Content-Type", "text/plain; charset=UTF-8");
			message.setFrom(new InternetAddress(msgSender));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(msgReceiver));
			message.setSubject(finalSubject);
			message.setText(finalMessageBody);
		
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendLive(String msgReceiver,String msgSender,String password,String subject,String messageBody){
		
		final String sender=msgSender;
		final String senderPassword=password;
		final String finalSubject=subject;
		final String finalMessageBody=messageBody;
		
		Properties props=new Properties();
		 props.setProperty("mail.transport.protocol", "smtp");
		    props.setProperty("mail.host", "smtp.live.com");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.port", "587");
		    props.put("mail.smtp.starttls.enable", "true");
		    
		    Session session=Session.getInstance(props, new Authenticator() {
		    	protected PasswordAuthentication getPasswordAuthentication(){
		    			return new PasswordAuthentication(sender, senderPassword);
		    	}
			});
		    try{
		    	Message message = new MimeMessage(session);
				  message.setHeader("Content-Type", "text/plain; charset=UTF-8");
				message.setFrom(new InternetAddress(msgSender));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(msgReceiver));
				message.setSubject(finalSubject);
				message.setText(finalMessageBody);
	
				Transport.send(message);
	 
				System.out.println("Done");
		    
		    }catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		    
	}
	
}
