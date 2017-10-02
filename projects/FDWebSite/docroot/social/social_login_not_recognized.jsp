<%@ page import="java.net.*,java.util.HashMap"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>

<%@ taglib uri="freshdirect" prefix="fd" %>

<html lang="en-US" xml:lang="en-US">
<head>
    <title>FreshDirect</title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>

<body>
	<center>
		<div id="sulCont" class="signup-style-social signin-social-login-not-recognized">
			<%
				//HashMap socialUser = (HashMap)session.getAttribute(SessionName.SOCIAL_USER);
				//String provider = (String)socialUser.get("provider");
				String provider = "facebook";
			%>
    			
    		<div class="form-side-social-header">Oops!</div>
			<div class="signin-social-forgot-pass-header-message">
				Your account is not connected to <%= provider %>.
				</br>Sign in to connect your accounts.							
			</div>
			<button class="social-login-green-button"
				onclick="window.parent.FreshDirect.components.ifrPopup.close(); window.parent.FreshDirect.modules.common.login.socialLogin();">
				Sign In
			</button>
			<div class="social-login-green-button-space"></div>
			<button class="social-login-green-button" onclick="window.parent.FreshDirect.components.ifrPopup.open({ url: '/social/signup_lite.jsp', height: 590, opacity: .5}) ">
				Create Account
			</button>
		</div>
	</center>
	<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>