<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <title>Message content</title>
    <style>
       html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            overflow: hidden;
        }
    #topRectangle {
        width: 100%;
        height: 50px;
        background-color: #99C3C3;
        box-shadow: 0 1px 2px rgba(0, 0, 0, .1);
    }
    #leftRectangle {
        margin-top: 0px;
        height: 617px;
        width: 200px;
        background-color: #B4DDE2;
    }
        #btnNew{
            margin-top:12px;
            margin-left:12px;
            background-color:transparent;
            color:white;
           font-size:medium;
        }

        #menuRows {
            padding-top: 15px;
            margin-left: 36px;
        }

        #menuButtons {
            background-color: transparent;
            color: black;
            font-size:large;
           margin-top:5px;
           margin-left:-30px;
        }

        #username{
            margin-left:150px;
            color:white;
            font-size:medium;
        }

    #inputEmail {
        background-color:white;
        width:170px;
        font-size: small;
        margin-top: 55px;
        margin-left: -30px;
        height:20px;
     

    }
    
    #lblMail{
    	font-size:large;
    	margin-left: 13px;
    	
    }		
	
	#allEmails{
		margin-top: 20px;
	}
	
	#mailOptions{
		margin-left:12px;
	}
	
	#inputEmailPassword{
		   height:20px;
		    margin-left: -30px;
		     width:170px;
	}
	
	#emailMessage{
		margin-left:200px;
		margin-top:-380px;
		width:1200px;
		text-align: left;
		padding-top: 15px;
		
	}
	
	#linkSubjects{
		height:20px;
		width:500px;
		
		text-decoration:none;
		width: 100%;
		
		
	
	}
	


    </style>
   	
   	
  
    
</head>
<body>
    <div id="topRectangle">
    	<form action="homepage/newmail" method="get">
    		<input id="btnNew" type="submit" value="New">
    		  <span id="username">${user.firstName }</span>
    	  <span style="float: right; margin-top: 15px; margin-right: 15px;  "><a href="/mail-client/logout" style="color: white; font-size: medium;  ">Logout</a></span>
    		  
    	</form>
       
     
    </div>
    
    <div id="leftRectangle">

        <div id="menuRows">
           <table>
                 <tr><td><form action="https://localhost:8443/mail-client/inbox" method="get"><input type="submit" value="Inbox" id="menuButtons"/></form></td></tr>
               <tr><td><form action="junk"><input type="submit" value="Junk" id="menuButtons" /></form></td></tr>
               <tr><td><form action="https://localhost:8443/mail-client/sent"><input type="submit" value="Sent" id="menuButtons" /></form></td></tr>
               <tr><td><form action="deleted"><input type="submit" value="Deleted" id="menuButtons" /></form></td></tr>
               <tr>
               	<td>
               		<form action="addmail" method="post">
               				<input type="text" id="inputEmail" placeholder="Add e-mail account" name="mail">
               				<input type="password" id="inputEmailPassword" placeholder="password" name="newMailPassword">
               			<br>
               				<input type="submit" value="Add" id="menuButtons">
               		</form>
               	</td>
               </tr>
              
           </table>
        </div>
        <div id="allEmails">
					<label id="lblMail">Emails:</label>
						
						<select  id="mailOptions">
							<c:forEach items="${mails}" var="mail">
								<option name="mailChecked" value="${mail.key}">${mail.key}</option>
							</c:forEach>
						</select>
				
					
		</div>
		<div id="emailMessage">
	
			<p id="linkSubjects">${msgValue }</p>
		
		</div>
			
            
    </div>
    

</body>
</html>