<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='true' />
<%
	String setUserAgreedTo = NVL.apply(request.getParameter("setUserAgreedTo"), "");
	FDSessionUser sessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
	String userId = NVL.apply(sessionUser.getUser().getUserId(), "<i>anonymous</i>");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> User Alcohol Warning Agreement Modifier </title>
	</head>
<body>
	<h3>User Alcohol Warning Agreement Modifier <a href="./alcohol.jsp" style="float: right;"">Reload</a></h3>
	<hr />
	Manually set a User's Agreement to the Alcohol Warning message. If a user is in the session already, they will be shown here. Otherwise, UserId will say "anonymous". Be sure to refresh the page you are testing on so the new value will be picked up.
	<hr />
	<% if (sessionUser != null) { %>
		Current Session UserId: <%= userId %><br />
		<%
			if (!"".equals(setUserAgreedTo) && ("true".equalsIgnoreCase(setUserAgreedTo) || "false".equalsIgnoreCase(setUserAgreedTo))) {
				sessionUser.setHealthWarningAcknowledged(Boolean.parseBoolean(setUserAgreedTo));
				%>Has Agreed To Alcohol Disclaimer <b>Set As</b>: <%=setUserAgreedTo%><br /><%
			}else{
				%>Has Agreed To Alcohol Disclaimer: <%=sessionUser.isHealthWarningAcknowledged()%><br /><%
			}
		%>
	<% } %>
	Reset To: <a href="./alcohol.jsp?setUserAgreedTo=true">true</a> <a href="./alcohol.jsp?setUserAgreedTo=false">false</a><br />
	
</body>
</html>
