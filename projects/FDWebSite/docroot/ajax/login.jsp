<%@ page language="java" contentType="text/plain; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
%><%@ taglib uri='freshdirect' prefix='fd' 
%><%@page import="com.freshdirect.framework.webapp.ActionError"
%><%@page import="java.util.HashMap"%><%!
HashMap<String, String> errorCodes = new HashMap<String, String>();

public void jspInit(){
	errorCodes.put("email_format", "E-mail Address");
	errorCodes.put("userid", "E-mail Address");
	errorCodes.put("password", "Password");
}
%><%
String successPage = request.getParameter("successPage");
if (successPage == null) {      		
	    successPage = "/login/index.jsp";        
}
%><fd:LoginController successPage="<%= successPage %>" mergePage="/login/merge_cart.jsp"
result="result" ajax="true"><% 
successPage = (String) request.getAttribute("fd_successPage");
if (successPage == null)
	successPage = "ILLEGAL_SUCCESSPAGE";
%><%= successPage %>
<%
for (ActionError error : result.getErrors()) {
%><%= errorCodes.containsKey(error.getType()) ? errorCodes.get(error.getType()) + ": " : "" %><%= error.getDescription() %>
<%
	break; // get the first error only
}
%></fd:LoginController>