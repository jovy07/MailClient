<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <title>Delete folder</title>
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
            margin-left:280px;
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
            margin-left:-320px;
            color:white;
            width:25px;
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
		margin-left:-5px;
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
	
		width:1200px;
		text-align: left;
	
	}
	
	#linkSubjects{
		height:20px;
		text-decoration:underline;
		text-decoration:none;
		border-bottom-style:solid;
		border-width:2px;
		width: 1200px;
		border-color: #B4DDE2;
		
	
	}
	#linkSubjects:HOVER {
	background-color:  #B4DDE2;
	}

	a:HOVER {
	background-color:  #B4DDE2;
	}
	#btnDelete{
	margin-left:0px;
	margin-top:-25px;
	background-color:transparent;    
    color:white;
    font-size:medium;
	}
	

    </style>
   	
 	<script type="text/javascript">
 		
 	function filterMails(){
 		var checkedMail=document.getElementById('mailChecked').value;
 		alert(checkedMail);
 	}
 	function deleteFunction(){
 		var msgDelete=document.getElementById('idMsgDelete').value;
 		alert(msgDelete);
 	}
 	</script>
    
</head>
<body>
    <div id="topRectangle">
    	<form action="homepage/newmail" method="get">
    		<input id="btnNew" type="submit" value="New">
    		  <span id="username">${user.firstName }</span>
    	  <span style="float: right; margin-top: 15px; margin-right: 15px;  "><a href="/mail-client/logout" style="color: white; font-size: medium;  ">Logout</a></span>
   	</form>
   		<div id="emailMessage">
   		
			<form action="deletemessage" method="post" id="messageList">
				<span><input type="submit" value="Delete" id="btnDelete"></span><br><br>		
			<c:forEach items="${message}" var="msg">
				<c:forEach items="${msg }" var="msgItem">				
					<input type="checkbox" name="msgToDelete" value="${msgItem.key }" ><a id="linkSubjects" href="<c:url  value="deletedbox/${msgItem.key }"/>" style="text-decoration: none; color: black;">${msgItem.key }</a><br><br>
				</c:forEach>
			
			</c:forEach>
			
		</form>	
				
		</div>
   	
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
					<form action="filtermessages" method="post">
						<select  id="mailOptions" onchange="filterMails()" name="mailChecked">
							<c:forEach items="${mails}" var="mail">
								<option  value="${mail.key}" >${mail.key}</option>
							</c:forEach>
						</select>
						<input type="submit" value="Filter">		
					</form>						
		</div>
		
		
			
            
    </div>
    

</body>
</html>