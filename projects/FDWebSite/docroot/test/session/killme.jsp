<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.CookieMonster" %>
<%@ page import="java.util.Enumeration" %>
<html lang="en-US" xml:lang="en-US">
<body>
	<%
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (user!=null) {
		    user.touch();
		    if(user.getSessionEvent()!=null)
		    	user.getSessionEvent().setIsTimeout("N");
		}
		
		// clear session
		Enumeration<String> e = session.getAttributeNames();
		while (e.hasMoreElements()) {
		    String name = (String)e.nextElement();
		    session.removeAttribute(name);
		}
		session.invalidate();
	%>
	Session terminated.
	<% if (request.getParameter("logout") != null) { /* also log out completely */
		// remove cookie
		CookieMonster.clearCookie(response);
		%><br />Fully logged out. <%
	} %>
	<marquee direction="down" width="50%" height="50%" behavior="alternate" style="position: absolute; height: 50%; width: 50%; top: 25%; left: 25%;">
		<marquee behavior="alternate" style="">
			<span style="color: #fff;">Congratulations. You are dead</span>.
		</marquee>
	</marquee>
</body>
</html>