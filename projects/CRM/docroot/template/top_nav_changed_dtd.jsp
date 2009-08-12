<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ taglib uri='template' prefix='tmpl' %>

<jsp:include page="/shared/template/includes/server_info.jsp"/>
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>

	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>

	<%-- YUI --%>
	<!-- Sam Skin CSS -->
	<link rel="stylesheet" type="text/css" href="/assets/yui/container/assets/skins/sam/container.css">
	
<%@ include file="/shared/template/includes/yui.jspf" %>
<% if ("true".equals(request.getAttribute("needsCCL"))) {
%>
    <%@ include file="/shared/template/includes/ccl.jspf"%>
    <%@ include file="/common/template/includes/ccl_crm.jspf"%>

<%
    {
       String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
       if (onbeforeunload != null && onbeforeunload.length() > 0) {
%>
    <script language="javascript">
       window.onbeforeunload = <%= onbeforeunload %>;
    </script>
<%
       } // if
    } // local block
%>

<% } %>

	<script type="text/javascript" src="/assets/javascript/phone_number.js"></script>
</head>
<body onload="<%=request.getAttribute("bodyOnLoad")%>"
      onunload="<%=request.getAttribute("bodyOnUnload")%>"
>

<%@ include file="/includes/context_help.jspf" %>

    <%-- header on top and content below --%>

    <jsp:include page="/includes/main_nav.jsp"/>
	
    <jsp:include page='/includes/customer_header.jsp'/>
	
	<jsp:include page='/includes/case_header.jsp'/>

	<tmpl:get name="content"/>
	
	<div class="footer"><jsp:include page='/includes/copyright.jsp'/></div>
</body>
</html>
