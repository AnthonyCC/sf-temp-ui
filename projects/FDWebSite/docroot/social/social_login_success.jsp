<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="freshdirect" prefix="fd" %>

  <div class="social-login-spinner">
  <img src="/media_stat/images/navigation/spinner.gif" alt="spinner" class="fleft" />  
  </div>
    <fd:CheckLoginStatus />
    <fd:ExternalAccountController />
  <%  
  	/*
    String connectionToken  = (String) request.getParameter("connection_token"); 
    out.println("connectionToken: "+connectionToken);
    */
  %>
  

  
  