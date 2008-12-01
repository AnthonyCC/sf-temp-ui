<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri='template' prefix='tmpl' %>

<html>
<head>
<title>/ FreshDirect Transportation Admin : Logout /</title>
<link rel="stylesheet" href="css/transportation.css" type="text/css" />	
<script language="javascript" type="text/javascript">
function logOutApp() {
	//alert('Step1');	
	try{
	  var agt=navigator.userAgent.toLowerCase();
	  if (agt.indexOf("msie") != -1) {
	    // IE clear HTTP Authentication
	    document.execCommand("ClearAuthenticationCache");   
	    //alert('Step2'); 
	  } 
	  else {
		var xmlhttp = createXMLObject();
	    xmlhttp.open("GET",".force_logout_offer_login_mozilla",true,"logout","");
	    xmlhttp.send("");
	    xmlhttp.abort();
	    //alert('Step3');     
	  }
	
	} catch(e) {
	// There was an error
		alert("System Error. Please try again later!");
	}
}

function createXMLObject() {
	try {
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
		}
		// code for IE
		else if (window.ActiveXObject) {
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
	} catch (e) {
		xmlhttp=false
	}
	return xmlhttp;
}
</script>
</head>
<body marginwidth="0" marginheight="0" border="0" onLoad="logOutApp()">
	<% 
	try {
        session.invalidate();
    } catch (IllegalStateException ex) {                
        ex.printStackTrace();
    }
    response.sendRedirect("login.jsp");  
            
 %>
</body>
</html>

