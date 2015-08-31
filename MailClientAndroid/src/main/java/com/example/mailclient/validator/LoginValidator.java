package com.example.mailclient.validator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import com.example.mailclient.model.Login;



public class LoginValidator implements Validator {

	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return Login.class.isAssignableFrom(arg0);
	}

	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		Login login=(Login) arg0;
		
		if (login.getUsername() == null || login.getUsername().length() == 0) 
		{ 
			arg1.rejectValue("username", "error.empty.field", "Please Enter User Name"); 
		}
		
		if (login.getPassword() == null || login.getPassword().length() == 0)
		{ 
			arg1.rejectValue("password", "error.empty.field", "Please Enter Password");
		} 

		
		
	}

}
