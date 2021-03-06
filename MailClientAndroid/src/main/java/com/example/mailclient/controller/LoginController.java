package com.example.mailclient.controller;


import java.util.ArrayList;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.mailclient.auth.CustomAuthenticationProvider;
import com.example.mailclient.model.User;
import com.example.mailclient.service.UserService;
import com.example.mailclient.service.impl.GetAllMailMessages;
import com.example.mailclient.service.impl.HostServerElection;
import com.example.mailclient.service.impl.MessageService;
import com.example.mailclient.service.impl.MessageServiceReceiver;
import com.example.mailclient.service.impl.UserVerification;

@Controller
public class LoginController {

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

	

	@RequestMapping(value = "/signup")
	public ModelAndView signUpPage() {
		
		return new ModelAndView("signUp_page");
	}

	@RequestMapping(value = "/signin")
	public ModelAndView createUser(@RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String username,
			@RequestParam String password, @RequestParam String dob,
			HttpServletRequest request) {

		HashMap<String, String> mails = new HashMap<String, String>();
		ArrayList<HashMap<String, String>> inbox = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> sent = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> filteredMails = new HashMap<String, String>();
	
		 ArrayList<HashMap<String, String>> deleted=new ArrayList<HashMap<String,String>>();
		User user = new User(firstName, lastName, username, password, dob,
				mails, inbox, sent, filteredMails,deleted);

		if (!userVerification.validate(user)) {
			return new ModelAndView("login_failed");
		}

		userService.createUser(firstName, lastName, username, password, dob,
				mails, inbox, sent, filteredMails,deleted);

		return new ModelAndView("afterLogin");
	}


	
	@RequestMapping(value="/loginfailed",method=RequestMethod.GET)
	public ModelAndView loginFailed(){
		
		return new ModelAndView("login_failed");
	}

	

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logoutPage(HttpSession session) {
		session.removeAttribute("username");
		return new ModelAndView("login");
	}

	


}
