<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>

<%@ taglib uri="freshdirect" prefix="fd" %>

  <%@ include file="/common/template/includes/i_javascripts_browse.jspf" %>
  <% request.getSession(false).removeAttribute(SessionName.SOCIAL_LOGIN_SUCCESS); %>

  <div class="social-login-spinner">
  <img src="/media_stat/images/navigation/spinner.gif" class="fleft" />  
  </div>
    <fd:CheckLoginStatus />
    <fd:ExternalAccountController />
  <%  
  	/*
    String connectionToken  = (String) request.getParameter("connection_token"); 
    out.println("connectionToken: "+connectionToken);
    */
  %>
  

  
  