package com.example.mailclient.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="users")
public class User {

	@Id
	private String username;
	
	private String firstName;
	private String lastName;

	private String password;
	private String birthdate;
	


	public HashMap<String, String> mailList;
	
	public ArrayList<HashMap<String, String>> inbox;
	
	public ArrayList<HashMap<String, String>> sent;
	
	public ArrayList<HashMap<String, String>> deleted;
	
	public HashMap<String, String> filteredMails;
	
	public User(String firstName, String lastName, String username,
			String password, String birthdate,HashMap<String, String> mails,ArrayList<HashMap<String, String>> inboxx,ArrayList<HashMap<String, String>> sentt,HashMap<String, String> filteredMailss, ArrayList<HashMap<String, String>> deletedd) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.birthdate = birthdate;
		this.mailList=mails;
		this.inbox=inboxx;
		this.sent=sentt;
		this.filteredMails=filteredMailss;
		this.deleted=deletedd;
		
	}



	public HashMap<String, String> getMailList() {
		return mailList;
	}



	public void setMailList(HashMap<String, String> mailList) {
		this.mailList = mailList;
	}



	public ArrayList<HashMap<String, String>> getInbox() {
		return inbox;
	}



	public void setInbox(ArrayList<HashMap<String, String>> inbox) {
		this.inbox = inbox;
	}



	public ArrayList<HashMap<String, String>> getSent() {
		return sent;
	}



	public void setSent(ArrayList<HashMap<String, String>> sent) {
		this.sent = sent;
	}



	public ArrayList<HashMap<String, String>> getDeleted() {
		return deleted;
	}



	public void setDeleted(ArrayList<HashMap<String, String>> deleted) {
		this.deleted = deleted;
	}



	public HashMap<String, String> getFilteredMails() {
		return filteredMails;
	}



	public void setFilteredMails(HashMap<String, String> filteredMails) {
		this.filteredMails = filteredMails;
	}



	public User(){
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}


	public void addMail(String mail,String newMailPassword){
		try{
			mailList.put(mail,newMailPassword);
		
		}catch(NullPointerException npe){
			System.out.println(npe.getMessage());
			
		}
	}
	
	
	
	
}
