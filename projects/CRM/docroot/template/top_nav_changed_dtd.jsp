<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ taglib uri='template' prefix='tmpl' %>

<jsp:include page="/shared/template/includes/server_info.jsp"/>
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>

	<link href="/assets/css/giftcards.css" rel="stylesheet" type="text/css" />
	<link href="/assets/css/modalbox.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" type="text/css">
         
	<script src="/assets/javascript/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="/assets/javascript/scriptaculous/1.9.0/scriptaculous.js?load=effects,builder" type="text/javascript" language="javascript"></script>
	<script  src="/assets/javascript/modalbox.js" type="text/javascript" language="javascript"></script>
	<script  src="/assets/javascript/FD_GiftCards.js" type="text/javascript" language="javascript"></script>
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>

	<%-- YUI --%>
	<!-- Sam Skin CSS -->
	<%-- when upgrading yui please create a new directory for the version --%>
	<link rel="stylesheet" type="text/css" href="/assets/yui-2.9.0/container/assets/skins/sam/container.css">
	
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

	<script language="javascript">
		/* temp fix for CRM errors */
		var $E  = YAHOO.util.Event; 	 
		var $D  = YAHOO.util.Dom; 	 
		//var $DH = YAHOO.ext.DomHelper; 	 
		var $C  = YAHOO.util.Connect; 	 
		var $X  = YAHOO.ext;
	</script>
</head>
<body onload="<%=request.getAttribute("bodyOnLoad")%>"
      onunload="<%=request.getAttribute("bodyOnUnload")%>"
>
<div class="tophelp1">
	<%@ include file="/includes/context_help.jspf" %>

    <%-- header on top and content below --%>

    <jsp:include page="/includes/main_nav.jsp"/>
	
    <jsp:include page='/includes/customer_header.jsp'/>
	
	<jsp:include page='/includes/case_header.jsp'/>

	<tmpl:get name="content"/>
</div>
	
	<div class="footer"><jsp:include page='/includes/copyright.jsp'/></div>
</body>
</html>
