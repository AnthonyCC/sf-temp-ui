<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<%@ page import="java.util.*"
%><%@ page import="com.freshdirect.fdstore.customer.FDIdentity"
%><%@ page import="com.freshdirect.test.TestSupport"
%><%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"
%><%@ page import="com.freshdirect.fdstore.customer.FDUserI"
%><%@ page import="com.freshdirect.smartstore.fdstore.VariantSelector"
%><%@ page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"
%><%@ page import="com.freshdirect.smartstore.RecommendationService"
%><%@ page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%
	FDUserI user = (FDUserI) session.getAttribute("fd.user");
%>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>What is my variant?</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="Sebesty�n G�bor">
	<!-- Date: 2008-06-09 -->
</head>
<fd:CheckLoginStatus/>
<% request.setAttribute("ss_custInfo", Boolean.TRUE); %>
<body>
<%@ include file="/shared/includes/ss_cust_info.jspf" %>
</body>
</html>
