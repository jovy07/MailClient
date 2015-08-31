package com.example.mailclient.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
public class MessagesController {

	
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
	
	

	@RequestMapping(value = "/sent", method = RequestMethod.GET)
	public ModelAndView sentPage(HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);
		sentMessages = userNew.sent;
		emails = userService.getEmails(username);
		Collections.reverse(sentMessages);
		model.put("message", sentMessages);
		model.put("mails", emails);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("sent_page", model);
	}

	@RequestMapping(value = "homepage/sent", method = RequestMethod.GET)
	public ModelAndView sentPageHomePage(HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);
		sentMessages = userNew.sent;
		emails = userService.getEmails(username);
		Collections.reverse(sentMessages);
		model.put("message", sentMessages);
		model.put("mails", emails);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("sent_page", model);
	}

	@RequestMapping(value = "sentbox/{msgItem}")
	public ModelAndView sentBoxPage(@PathVariable String msgItem,
			HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String msgValue = "";

		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);

		sentMessages = userNew.sent;

		for (HashMap<String, String> messages : sentMessages) {

			for (String key : messages.keySet()) {

				String formattedKey = key.substring(0, key.length() - 1);

				if (formattedKey.matches(msgItem)) {

					msgValue = messages.get(key);

				}
			}
		}
		emails = userService.getEmails(username);
		model.put("mails", emails);
		model.put("msgValue", msgValue);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("sentbox_item", model);

	}

	@RequestMapping(value = "homepage/sentbox/{msgItem}")
	public ModelAndView sentBoxPageHomePage(@PathVariable String msgItem,
			HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String msgValue = "";
		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);

		sentMessages = userNew.sent;
		emails = userService.getEmails(username);
		for (HashMap<String, String> messages : sentMessages) {

			for (String key : messages.keySet()) {

				String formattedKey = key.substring(0, key.length() - 1);

				if (formattedKey.matches(msgItem)) {

					msgValue = messages.get(key);

				}
			}
		}

		model.put("mails", emails);
		model.put("msgValue", msgValue);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("sentbox_item", model);

	}
	@RequestMapping(value = "messagebox/{msgItem}")
	public ModelAndView messageBoxPage(@PathVariable String msgItem,
			HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String msgValue = "";

		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);
	
		content = userNew.inbox;

		for (HashMap<String, String> messages : content) {

			for (String key : messages.keySet()) {

				String formattedKey = key.substring(0, key.length() - 1);
				
				if (formattedKey.matches(msgItem)) {

					msgValue = messages.get(key);

				}
			}
		}
		emails = userService.getEmails(username);

		model.put("mails", emails);
		model.put("msgValue", msgValue);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("message_item", model);

	}

	@RequestMapping(value = "homepage/messagebox/{msgItem}")
	public ModelAndView messageBoxPageHomePage(@PathVariable String msgItem,
			HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String msgValue = "";
		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);

		content = userNew.inbox;

		for (HashMap<String, String> messages : content) {

			for (String key : messages.keySet()) {

				String formattedKey = key.substring(0, key.length() - 1);

				if (formattedKey.matches(msgItem)) {

					msgValue = messages.get(key);

				}
			}
		}
		emails = userService.getEmails(username);
		model.put("mails", emails);
		model.put("msgValue", msgValue);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("message_item", model);

	}
	

	@RequestMapping(value="/deleted")
	public ModelAndView deleteFolder(HttpSession session){
		String username=(String) session.getAttribute("username");
		Map<String, Object> model = new HashMap<String, Object>();
		User newUser=userService.findUser(username);
		Collections.reverse(newUser.deleted);
		emails = userService.getEmails(username);
		model.put("mails", emails);
		model.put("message",newUser.deleted );
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("delete_folder",model);
	}
	
	@RequestMapping(value = "deletedbox/{msgItem}")
	public ModelAndView deletedBoxPage(@PathVariable String msgItem,
			HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String msgValue = "";

		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);

		content = userNew.deleted;
		

		for (HashMap<String, String> messages : content) {

			for (String key : messages.keySet()) {

				String formattedKey = key.substring(0, key.length() - 1);

				if (formattedKey.matches(msgItem)) {

					msgValue = messages.get(key);

				}
			}
		}
		emails = userService.getEmails(username);
		model.put("mails", emails);
		model.put("msgValue", msgValue);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("message_item", model);

	}
	@RequestMapping(value = "homepage/deletedbox/{msgItem}")
	public ModelAndView deletedBoxPageHomePage(@PathVariable String msgItem,
			HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String msgValue = "";

		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);

		content = userNew.deleted;

		for (HashMap<String, String> messages : content) {

			for (String key : messages.keySet()) {

				String formattedKey = key.substring(0, key.length() - 1);

				if (formattedKey.matches(msgItem)) {

					msgValue = messages.get(key);

				}
			}
		}
		emails = userService.getEmails(username);
		model.put("mails", emails);
		model.put("msgValue", msgValue);
		//String jsonString=new Gson().toJson(model).toString();
		//return jsonString;
		return new ModelAndView("message_item", model);

	}

}
