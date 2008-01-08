<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% request.getSession().invalidate(); %>
<html>
<head>
<title>/ FreshDirect Transportation Admin : Logout /</title>
<link rel="stylesheet" href="css/transportation.css" type="text/css" />	
<script language="javascript" type="text/javascript">
function logOutApp() {	
	try{
	  var agt=navigator.userAgent.toLowerCase();
	  if (agt.indexOf("msie") != -1) {
	    // IE clear HTTP Authentication
	    document.execCommand("ClearAuthenticationCache");    
	  } 
	  else {
	    var xmlhttp = createXMLObject();
	    xmlhttp.open("GET",".force_logout_offer_login_mozilla",true,"logout","");
	    xmlhttp.send("");
	    xmlhttp.abort();
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
	<table class="appframe" width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr class="apptitle" >
				<td width="40%">
					<img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban" />
				</td>
				<td width="60%">Transportation Department</td>
			</tr>
			<tr>
				<td class="navlist" colspan="3" bgcolor="c00cc3d">
				<table class="navtbl" border="0" width="100%">
					<tr>
						<td width="10%" align="center"><a href="index.jsp" >&nbsp;Home&nbsp;</a></td>
						<td width="90%" align="center">&nbsp;</td>
					</tr>
					</table>
				</td>
			</tr>
			
	</table>	
    <p />
		You have successfully logged out of Transporation Admin Application. 
				
	<br clear="all"/>
	<div class="separator"></div>
	<div class="footer"><jsp:include page='/common/copyright.jsp'/></div>
</body>
</html>

