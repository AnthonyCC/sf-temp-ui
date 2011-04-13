<%@ taglib uri='template' prefix='tmpl' %>

<html>
<head>
<title>Welcome to ERPSy-Daisy</title>
<style type="text/css">
	<!--
	body {
	margin: 6px;
	margin-top: 0px;
	height: 100%;
	
	background-color: #E7E7D6;
	
	font-family:  Trebuchet MS, Arial, Verdana, sans-serif;
	color: #000000;
	font-size: 10pt;
	scrollbar-base-color: #FFFFFF; 
	scrollbar-face-color: #FFFFFF; 
	scrollbar-track-color: #FFFFFF; 
	scrollbar-arrow-color: #666666;
	scrollbar-highlight-color: #CCCCCC; 
	scrollbar-3dlight-color: #999999; 
	scrollbar-shadow-color: #666666;
	scrollbar-darkshadow-color: #666666;
	}
	
	.main_nav {
	position: relative;
	width: 60%;
	height: auto;
	left: 0;
	top: 0;
	/*background:#FFFFFF;*/
	}
	
	/* LOGIN */
	
	.login_header {
	font-size: 18pt;
	color: #000000;
	}
	
	.login_field {
	font-size: 10pt;
	color: #000000;
	}
	
	/* LOGIN */
	
	.black1px{
	height: 1px;
	color: #000000;
	}
	
	.submit {
	background-color: #000000;
	font-size: 8pt;
	color: #FFFFFF;
	border: 1px #999999 solid;
	padding: 2px;
	padding-left: 0px;
	padding-right: 0px;
	margin-left: 5px;
	margin-right: 5px;
	}
	
	.input_text{
	background-color:#FFFFFF;
	font-size: 10pt;
	color: #000000;
	font-family:  Trebuchet MS, Arial, Verdana, sans-serif;
	}

	.error {
		font-size: 10pt; font-weight: bold; color: #CC0000;
	}
	
	.error_detail {
		font-size: 9pt; font-weight: bold; color: #CC0000;
	}
	
	.copyright {
	font-size: 8pt;
	color: #555555;
	}
	.login_form_title {
	font-size: 22px;
	font-weight: bold;
	}
	.login_fresh { color: #f93; font-size: 24px;}
	.login_direct { color: #693; font-size: 24px;}
	-->
	</style>
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
    response.sendRedirect(".");      
 %>
</body>
</html>

