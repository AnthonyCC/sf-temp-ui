<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>

<%@ taglib uri="freshdirect" prefix="fd" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>FreshDirect</title>
  <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	
	<style>
	.signup-style{
		
		width:500px; 
		height:190px; 
		overflow-y: auto; 
		overflow-x: hide;
		/*-webkit-box-shadow: 3px 3px 5px 6px #ccc;  /* Safari 3-4, iOS 4.0.2 - 4.2, Android 2.3+ */
   	   /* -moz-box-shadow:    3px 3px 5px 6px #ccc;  /* Firefox 3.5 - 3.6 */
       /*   box-shadow:         3px 3px 5px 6px #ccc;  /* Opera 10.5, IE 9, Firefox 4+, Chrome 6+, iOS 5 */
        /* border-radius:5px; /* Opera 10.5, IE 9, Firefox 4+, Chrome 6+, iOS 5 */
       /* -webkit-border-radius:5px; /* Safari 3-4, iOS 4.0.2 - 4.2, Android 2.3+ */
        /*-moz-border-radius:5px;  /* Firefox 3.5 - 3.6 */
       /* -o-border-radius:5px;*/
		padding-top:100px;
		}
   </style>  	

</head>

<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="10" topmargin="10" >

	<center>	
	
	
	
	<div id="sulCont" class="signup-style">

			<span style="font-size:12px;font-weight:bold;font-family: Verdana, Arial, sans-serif;margin-right:270px;">Check your email.</span>
			<br><br>
			<font><span style="font-size:12px; margin-right:140px;">A link to reset your password is on its way.</span></FONT>
		
	</div>

	</center>
	
</body>
</html>
