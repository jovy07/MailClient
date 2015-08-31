package com.example.mailclient.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class HomePageController {


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


	private HashMap<String, String> emails = new HashMap<String, String>();


	private User user;
	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public String testPut(HttpSession session,@RequestParam String username,@RequestParam String password){
		Authentication auth = customAuthProvider
				.authenticate(new UsernamePasswordAuthenticationToken(
						username,password));
		user = userService.findUser(auth.getName());
		
		session.setAttribute("username", user.getUsername());
		
		
		return "";
	}
	
	@RequestMapping(value = "/homepage", method = RequestMethod.POST)
	@ResponseBody
	public String userPage(@RequestParam String username,
			@RequestParam String password, HttpSession session)
			throws MessagingException, IOException {

		if (username.length() == 0 || password.length() == 0) {
			//return new ModelAndView("redirect:/loginfailed");
			return "";
		}
		try {
			Authentication auth = customAuthProvider
					.authenticate(new UsernamePasswordAuthenticationToken(
							username, password));
			user = userService.findUser(auth.getName());

			if (user == null) {
			
				//return new ModelAndView("redirect:/loginfailed");
				return "";
			}
			session.setAttribute("user", user);
			session.setAttribute("username", auth.getName());
					

			Map<String, Object> model = new HashMap<String, Object>();

			userService.refreshServer(username);
			

			User userNew = userService.findUser(username);
			sentMessages = userNew.sent;
			content = userNew.inbox;
			Collections.reverse(content);
			Collections.reverse(sentMessages);

			emails = userService.getEmails(username);
			model.put("mails", emails);
			model.put("message", content);

			String jsonString=new Gson().toJson(model).toString();
			return jsonString;
			//return new ModelAndView("user_page", model);
			
		} catch (NullPointerException nex) {
		
			//return new ModelAndView("redirect:/loginfailed");
			return "";
		}

	}
	@RequestMapping(value = "/homepage", method = RequestMethod.GET)
	@ResponseBody
	public String getUserPage(HttpSession session)
			throws MessagingException, IOException {
		if (session.getAttribute("username") != null) {

			String username = (String) session.getAttribute("username");

			Map<String, Object> model = new HashMap<String, Object>();
			User userNew = userService.findUser(username);
		
			content = userNew.inbox;
			Collections.reverse(content);
			model.put("message", content);

			emails = userService.getEmails(username);

			model.put("mails", emails);

			String jsonString=new Gson().toJson(model).toString();
			return jsonString;
			//return new ModelAndView("user_page", model);
		}

		if (session.getAttribute("username") == null) {
			//return new ModelAndView("redirect:/loginfailed");
			return "";
		}

		return null;

	}
	

	@RequestMapping(value = "/inbox", method = RequestMethod.GET)
	@ResponseBody
	public String inboxPage(HttpSession session) {
		
		String username = (String) session.getAttribute("username");
		
		Map<String, Object> model = new HashMap<String, Object>();
		User userNew = userService.findUser(username);
		emails = userService.getEmails(username);
		content = userNew.inbox;
		Collections.reverse(content);
		model.put("message", content);
		model.put("mails", emails);

		String jsonString=new Gson().toJson(model).toString();
		return jsonString;
		//return new ModelAndView("user_page", model);
	}

	@RequestMapping(value = "homepage/inbox", method = RequestMethod.GET)
	@ResponseBody
	public String inboxPageHomePage(HttpSession session) {

		
		String username = (String) session.getAttribute("username");
		Map<String, Object> model = new HashMap<String, Object>();
		User userNew = userService.findUser(username);
		emails = userService.getEmails(username);
		content = userNew.inbox;
		Collections.reverse(content);
		model.put("message", content);
		model.put("mails", emails);

		String jsonString=new Gson().toJson(model).toString();
		return jsonString;
		//return new ModelAndView("user_page", model);
	}

}
