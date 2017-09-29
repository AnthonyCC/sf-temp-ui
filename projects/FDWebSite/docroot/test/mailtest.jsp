<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="javax.mail.Session"%>
<%@page import="java.util.Properties"%>
<%@page import="javax.mail.Folder"%>
<%@page import="javax.mail.Store"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.Provider"%><html lang="en-US" xml:lang="en-US">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="en-US">
<title>Java Mail Test Page</title>
</head>
<body>
<%
	String host = request.getParameter("host");
	String user = request.getParameter("user");
	host = host == null ? "localhost" : host;
	user = user == null ? "username" : user;
%>
<form action="<%= request.getRequestURI() %>" method="get">
<input type="text" name="user" value="<%= user %>">@<input type="text" name="host" value="<%= host %>"> <input type="submit" value="Submit">
</form>
<%
	Properties p = new Properties();
	p.put("mail.host", "localhost");
	p.put("mail.store.protocol", "pop3");
	p.put("mail.transport.protocol", "smtp");
	Session sess = Session.getDefaultInstance(p);
	boolean found = false;
	for (Provider provider : sess.getProviders()) {
		if (provider.getClassName().contains("mock_javamail")) {
			found = true;
			break;
		}
	}
	if (!found)
		throw new IllegalStateException("mock javamail is not configured properly. Add mock-javamail JAR file to the CLASSPATH");
	Store store = sess.getStore("pop3");
	store.connect(host, user, "password");
	Folder folder = store.getFolder("INBOX");
	folder.open(Folder.READ_WRITE);
	Message[] msgs = folder.getMessages();
	int i = 1;
%>
<% for (Message msg : msgs) {
%>
	<h1>Message #<%= i %></h1>
	<%= msg.getContent() %>
<% i++; } %>
</body>
</html>