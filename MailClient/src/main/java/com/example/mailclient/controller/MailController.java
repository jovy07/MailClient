package com.example.mailclient.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.mailclient.auth.CustomAuthenticationProvider;
import com.example.mailclient.model.User;
import com.example.mailclient.service.UserService;
import com.example.mailclient.service.impl.GetAllMailMessages;
import com.example.mailclient.service.impl.HostServerElection;
import com.example.mailclient.service.impl.MessageService;
import com.example.mailclient.service.impl.MessageServiceReceiver;
import com.example.mailclient.service.impl.UserVerification;
import com.google.gson.Gson;


@Controller
public class MailController {

	@Autowired
	private CustomAuthenticationProvider customAuthProvider;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService msgService;

	@Autowired
	private UserVerification userVerification;

	@Autowired
	private MessageServiceReceiver messageServiceReceiver;

	@Autowired
	private HostServerElection hostServerElection;

	@Autowired
	private GetAllMailMessages getAllEmailMessages;

	private ArrayList<HashMap<String, String>> content = new ArrayList<HashMap<String, String>>();

	private ArrayList<HashMap<String, String>> sentMessages = new ArrayList<HashMap<String, String>>();

	private HashMap<String, String> sentMessagesEmails = new HashMap<String, String>();

	private HashMap<String, String> emails = new HashMap<String, String>();

	private HashMap<String, String> filterMessages = new HashMap<String, String>();

	private User user;
	
	
	@RequestMapping(value = "/addmail", method = RequestMethod.POST)
	public ModelAndView addNewMail(@RequestParam String mail,
			HttpSession session, @RequestParam String newMailPassword)
			throws MessagingException, IOException {

		String username = (String) session.getAttribute("username");

		userService.addMail(username, mail, newMailPassword);

		Map<String, Object> model = new HashMap<String, Object>();

		emails = userService.getEmails(username);

		userService.refreshServer(username);
		
		User userNew = userService.findUser(username);

		content = userNew.inbox;
		Collections.reverse(content);
		model.put("message", content);
		model.put("mails", emails);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("user_page", model);
	}
	@RequestMapping(value = "/addmail", method = RequestMethod.GET)
	public ModelAndView returnToHome(HttpSession session) {
		String username = (String) session.getAttribute("username");
		Map<String, Object> model = new HashMap<String, Object>();

		emails = userService.getEmails(username);
		User userNew = userService.findUser(username);

		content = userNew.inbox;
		Collections.reverse(content);
		model.put("message", content);
		model.put("mails", emails);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("user_page", model);
	}
	
	@RequestMapping(value = "homepage/newmail")
	public ModelAndView sendMail(HttpSession session) {
		Map<String, Object> model = new HashMap<String, Object>();
		String username = (String) session.getAttribute("username");
		emails = userService.getEmails(username);
		model.put("mails", emails);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("send_email", model);
	}

	@RequestMapping(value = "homepage/sendmail", method = { RequestMethod.POST }, params = {
			"sender", "recipient" })
	public ModelAndView sendEmailtoReceiver(
			@RequestParam(value = "sender") String sender,
			@RequestParam(value = "recipient") String recipient,
			@RequestParam String subject, @RequestParam String msg,
			HttpSession session) {

		String keyRecipient = "";
		keyRecipient += recipient.replaceAll(",", ".") + ":" + subject + "....";
		sentMessagesEmails.put(keyRecipient, msg);

		String username = (String) session.getAttribute("username");

		userService.saveToSent(username, sentMessagesEmails);

		String senderDot = sender.replaceAll(",", ".");
		String recipientDot = recipient.replaceAll(",", ".");
		String password = "";

		User user= userService.findUser(username);
		
		for (String key : user.mailList.keySet()) {
			
			if (senderDot.matches(key)) {

				password = user.mailList.get(key);
			}
		}

		if (senderDot.contains("live") || senderDot.contains("hotmail")) {
			msgService
					.sendLive(recipientDot, senderDot, password, subject, msg);
		} else {
			msgService.sendGmail(recipientDot, senderDot, password, subject,
					msg);
		}
		Map<String, Object> model = new HashMap<String, Object>();
		emails = userService.getEmails(username);
		model.put("mails", emails);
		User userNew = userService.findUser(username);
		content = userNew.inbox;
		Collections.reverse(content);
		model.put("message", content);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("user_page", model);
	}
	

	@RequestMapping(value = "homepage/sendmail", method = RequestMethod.GET, params = {
			"sender", "recipient" })
	public ModelAndView sendEmailtoReceiverGetMethod(
			@RequestParam(value = "sender") String sender,
			@RequestParam(value = "recipient") String recipient,HttpSession session) {
		Map<String, Object> model = new HashMap<String, Object>();
		String username=(String) session.getAttribute("username");
		User userNew = userService.findUser(username);
		content = userNew.inbox;
		emails = userService.getEmails(username);
		Collections.reverse(content);
		model.put("mails", emails);
		model.put("message", content);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("user_page", model);
	}


}
