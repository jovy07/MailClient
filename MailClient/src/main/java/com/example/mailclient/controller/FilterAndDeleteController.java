package com.example.mailclient.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
public class FilterAndDeleteController {

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
	
	@RequestMapping(value = "/filtermessages", method = RequestMethod.POST)
	public ModelAndView filterMessages(@RequestParam String mailChecked,
			HttpSession session) {

		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);

		filterMessages = userNew.filteredMails;
		ArrayList<HashMap<String, String>> filterMessagesList = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> finalFilterMessage = new HashMap<String, String>();

		ArrayList<String> matchedKeys = new ArrayList<String>();

		for (String key : filterMessages.keySet()) {

			if (mailChecked.matches(key)) {

				matchedKeys.add(filterMessages.get(key));

			}
		}

		for (String key : matchedKeys) {

			for (HashMap<String, String> messages : userNew.inbox) {

				for (String keyToMatch : messages.keySet()) {

					if (key.matches(keyToMatch)) {
						finalFilterMessage.put(key, messages.get(key));
						filterMessagesList.add(finalFilterMessage);
					}

				}
			}
		}
		session.setAttribute("mailChecked", mailChecked);
		session.setAttribute("filteredMessage", filterMessagesList);

		Map<String, Object> model = new HashMap<String, Object>();
		emails = userService.getEmails(username);
		model.put("filteredMail", mailChecked);
		model.put("mails", emails);
		model.put("message", filterMessagesList);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("filter_message_page", model);
	}

	@RequestMapping(value = "/filtermessages", method = RequestMethod.GET)
	public ModelAndView filterMessagesGetMethod(HttpSession session) {

		String username = (String) session.getAttribute("username");
		String mailChecked = (String) session.getAttribute("mailChecked");
		ArrayList<HashMap<String, String>> filterMessagesList = (ArrayList<HashMap<String, String>>) session
				.getAttribute("filteredMessage");

		Map<String, Object> model = new HashMap<String, Object>();
		emails = userService.getEmails(username);
		model.put("filteredMail", mailChecked);
		model.put("mails", emails);
		model.put("message", filterMessagesList);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("filter_message_page", model);
	}
	

	@RequestMapping(value="/deletemessage",method=RequestMethod.POST)
	public ModelAndView deleteMessages(@RequestParam String msgToDelete,HttpSession session){
		
		String username=(String) session.getAttribute("username");
		Map<String, Object> model = new HashMap<String, Object>();
		emails = userService.getEmails(username);
		model.put("mails", emails);
		userService.deleteMessage(username, msgToDelete);
		
		User newUser=userService.findUser(username);
		content=newUser.inbox;
		Collections.reverse(content);
		model.put("message", content);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("user_page",model);
	}
}
