<%@ page language="java" contentType="text/plain; charset=ISO-8859-1"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<fd:CheckLoginStatus />
<%
	request.setAttribute("is_ajax", "true");
	ActionResult result = new ActionResult();
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
%>
<%@ include file="/includes/smartstore/i_dyf.jspf" %>