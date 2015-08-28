package com.example.mailclient.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	private ArrayList<HashMap<String, String>> content = new ArrayList<HashMap<String, String>>();

	private ArrayList<HashMap<String, String>> sentMessages = new ArrayList<HashMap<String, String>>();

	private HashMap<String, String> sentMessagesEmails = new HashMap<String, String>();

	private HashMap<String, String> emails = new HashMap<String, String>();

	private HashMap<String, String> filterMessages = new HashMap<String, String>();

	private User user;

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

	@RequestMapping(value = "/homepage", method = RequestMethod.POST)
	public ModelAndView userPage(@RequestParam String username,
			@RequestParam String password, HttpSession session)
			throws MessagingException, IOException {

		if (username.length() == 0 || password.length() == 0) {
			return new ModelAndView("redirect:/");
		}
		try {
			Authentication auth = customAuthProvider
					.authenticate(new UsernamePasswordAuthenticationToken(
							username, password));
			user = userService.findUser(auth.getName());

			if (user == null) {
				System.out.println("kravaaaaaaaaaaaaaaaaaa");
				return new ModelAndView("login_failed");
			}
			session.setAttribute("user", user);
			session.setAttribute("username", auth.getName());

			if (!user.getPassword().matches(password)) {

				return new ModelAndView("redirect:/");
			}

			Map<String, Object> model = new HashMap<String, Object>();

			userService.refreshServer(username);
			// content =getAllEmailMessages.getAllMessages(user.mailList,
			// username);

			User userNew = userService.findUser(username);
			sentMessages = userNew.sent;
			content = userNew.inbox;

			emails = userService.getEmails(username);
			model.put("mails", emails);
			model.put("message", content);

			return new ModelAndView("user_page", model);

		} catch (NullPointerException nex) {

			return new ModelAndView("login_failed");

		}

	}

	@RequestMapping(value = "/homepage", method = RequestMethod.GET)
	public ModelAndView getUserPage(HttpSession session)
			throws MessagingException, IOException {
		if (session.getAttribute("username") != null) {

			String username = (String) session.getAttribute("username");

			Map<String, Object> model = new HashMap<String, Object>();
			User userNew = userService.findUser(username);
			// sentMessages=userNew.sent;
			content = userNew.inbox;
			model.put("message", content);

			emails = userService.getEmails(username);

			model.put("mails", emails);
			return new ModelAndView("user_page", model);
		}

		if (session.getAttribute("username") == null) {
			return new ModelAndView("redirect:/");
		}

		return null;

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logoutPage(HttpSession session) {
		session.removeAttribute("username");
		return new ModelAndView("login");
	}

	@RequestMapping(value = "/addmail", method = RequestMethod.POST)
	public ModelAndView addNewMail(@RequestParam String mail,
			HttpSession session, @RequestParam String newMailPassword)
			throws MessagingException, IOException {

		String username = (String) session.getAttribute("username");

		userService.addMail(username, mail, newMailPassword);

		Map<String, Object> model = new HashMap<String, Object>();

		emails = userService.getEmails(username);

		userService.refreshServer(username);
		// content =getAllEmailMessages.getAllMessages(user.mailList, username);
		User userNew = userService.findUser(username);

		content = userNew.inbox;
		model.put("message", content);
		model.put("mails", emails);

		return new ModelAndView("user_page", model);
	}

	@RequestMapping(value = "/addmail", method = RequestMethod.GET)
	public ModelAndView returnToHome(HttpSession session) {
		String username = (String) session.getAttribute("username");
		Map<String, Object> model = new HashMap<String, Object>();

		emails = userService.getEmails(username);
		User userNew = userService.findUser(username);

		content = userNew.inbox;
		model.put("message", content);
		model.put("mails", emails);
		return new ModelAndView("user_page", model);
	}

	@RequestMapping(value = "/inbox", method = RequestMethod.GET)
	public ModelAndView inboxPage(HttpSession session) {

		String username = (String) session.getAttribute("username");
		Map<String, Object> model = new HashMap<String, Object>();
		User userNew = userService.findUser(username);

		content = userNew.inbox;
		model.put("message", content);
		model.put("mails", emails);
		return new ModelAndView("user_page", model);
	}

	@RequestMapping(value = "homepage/inbox", method = RequestMethod.GET)
	public ModelAndView inboxPageHomePage(HttpSession session) {

		String username = (String) session.getAttribute("username");
		Map<String, Object> model = new HashMap<String, Object>();
		User userNew = userService.findUser(username);

		content = userNew.inbox;
		model.put("message", content);
		model.put("mails", emails);
		return new ModelAndView("user_page", model);
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

		model.put("mails", emails);
		model.put("msgValue", msgValue);
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

		model.put("mails", emails);
		model.put("msgValue", msgValue);
		return new ModelAndView("message_item", model);

	}

	@RequestMapping(value = "homepage/newmail")
	public ModelAndView sendMail() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("mails", emails);

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

		for (String key : user.mailList.keySet()) {
			System.out.println(key);
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
		model.put("mails", emails);
		User userNew = userService.findUser(username);
		content = userNew.inbox;
		model.put("message", content);
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
		model.put("mails", emails);
		model.put("message", content);
		return new ModelAndView("user_page", model);
	}

	@RequestMapping(value = "/sent", method = RequestMethod.GET)
	public ModelAndView sentPage(HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);
		sentMessages = userNew.sent;
		model.put("message", sentMessages);
		model.put("mails", emails);
		return new ModelAndView("sent_page", model);
	}

	@RequestMapping(value = "homepage/sent", method = RequestMethod.GET)
	public ModelAndView sentPageHomePage(HttpSession session) {

		Map<String, Object> model = new HashMap<String, Object>();
		String username = (String) session.getAttribute("username");
		User userNew = userService.findUser(username);
		sentMessages = userNew.sent;
		model.put("message", sentMessages);
		model.put("mails", emails);
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

		model.put("mails", emails);
		model.put("msgValue", msgValue);
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
		return new ModelAndView("sentbox_item", model);

	}

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
		
		model.put("message", content);
		return new ModelAndView("user_page",model);
	}
	
	@RequestMapping(value="/deleted")
	public ModelAndView deleteFolder(HttpSession session){
		String username=(String) session.getAttribute("username");
		Map<String, Object> model = new HashMap<String, Object>();
		User newUser=userService.findUser(username);
		emails = userService.getEmails(username);
		model.put("mails", emails);
		model.put("message",newUser.deleted );
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

		model.put("mails", emails);
		model.put("msgValue", msgValue);
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

		model.put("mails", emails);
		model.put("msgValue", msgValue);
		return new ModelAndView("message_item", model);

	}


}
