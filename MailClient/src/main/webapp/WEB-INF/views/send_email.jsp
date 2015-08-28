<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>Send email to someone</title>
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

    #btnNew {
        margin-top: 12px;
        margin-left: 12px;
        background-color: transparent;
        color: white;
        font-size: medium;
    }

    #menuRows {
        padding-top: 15px;
        margin-left: 36px;
    }

    #menuButtons {
        background-color: transparent;
        color: black;
        font-size: large;
        margin-top: 5px;
        margin-left: -30px;
    }

    #username {
        margin-left: 150px;
        color: white;
        font-size: medium;
    }

    #inputEmail {
        background-color: white;
        width: 170px;
        font-size: small;
        margin-top: 55px;
        margin-left: -30px;
        height: 20px;
    }

    #lblMail {
        font-size: large;
        margin-left: 13px;
        margin-top:300px;
      
    }

    #allEmails {
        margin-top: 50px;
        margin-left:-10px;
    }

    #mailOptions {
        margin-left: 12px;
    }

    #inputEmailPassword {
        height: 20px;
        margin-left: -30px;
        width: 170px;
    }

    #emailMessage {
        margin-left: 200px;
        margin-top: -380px;
        width: 1200px;
        text-align: left;
        padding-top: 15px;
    }

    #linkSubjects {
        height: 20px;
        width: 500px;
        text-decoration: underline;
        text-decoration: none;
        border-bottom-style: solid;
        border-width: 2px;
        width: 100%;
        border-color: #B4DDE2;
    }

        #linkSubjects:HOVER {
            background-color: #B4DDE2;
        }

    a:HOVER {
        background-color: #B4DDE2;
    }

    #mailFormDiv{
        margin-top:25px;
        margin-left:200px;
        width:1300px;

    }

    #inputSubject{
       
        width:1300px;
    }

    #messageArea{
        width:1300px;
        height:500px;
    }

    #sendButton{
        margin-left:200px;
        margin-top:15px;
    }
</style>

<script type="text/javascript">
	
   	function getRecMail(){
		var recipient=document.getElementById('recipientMail').value;
		var recipientDot=recipient.replace(/\./g,',');
		
		var recipientDotEncoded=encodeURIComponent(recipientDot);
		
		var sender=document.getElementById('mailOptions').value;
		var senderDot=sender.replace(/\./g,',');
		var senderDotEncoded=encodeURIComponent(senderDot);
		var url=recipientDotEncoded+'&'+senderDotEncoded;
	
		
		document.getElementById('sendMail').action='sendmail?sender='+senderDotEncoded+'&recipient='+recipientDotEncoded;
	
		
	
	}

   	
</script>


<body>
    <div id="topRectangle">
    	<form action="" id="sendMail" method="post" >
    		<input type="submit"  id="sendButton" value="Send" onclick="getRecMail()" >
    		
    		 <div id="mailFormDiv">
          Subject:<br>
                <input type="text" name="subject" id="inputSubject" ><br>
               <br />
                <br />
               Add Message:<br>
                <textarea id="messageArea" name="msg"></textarea>
               <br />
  
        </div>
    	</form>
        
    </div>

    <div id="leftRectangle">

        <div>

            To:<br>
            <input type="text" name="recipientMail" id="recipientMail"><br>
           
            <div id="allEmails">
                <label id="lblMail">Emails:</label>
                <select id="mailOptions" onchange="getRecMail()">
                	<c:forEach items="${mails}" var="mail">
                		  <option name="mailChecked" value="${mail.key}" id="mailChecked"  >${mail.key}</option>
                	</c:forEach>
                    
                </select>


            </div>
        </div>
      


    </div>


</body>
</html>