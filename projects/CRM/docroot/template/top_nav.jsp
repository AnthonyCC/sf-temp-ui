<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="java.util.Calendar" %>
<%@ taglib uri='template' prefix='tmpl' %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<jsp:include page="/shared/template/includes/server_info.jsp"/>
<html>
<head>
	<title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>

	<meta http-equiv="X-UA-Compatible" content="IE=8">
	<link rel="stylesheet" type="text/css" href="/ccassets/javascript/timepicker/picker.css" >
	<link rel="stylesheet" type="text/css" href="/assets/css/giftcards.css" />
	<link rel="stylesheet" type="text/css" href="/assets/css/modalbox.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/css/crm.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/css/case.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/css/promo.css" />
	<link rel="stylesheet" type="text/css" href="/assets/css/timeslots.css"/>
	<%-- YUI --%>
	<!-- Sam Skin CSS -->
	<link rel="stylesheet" type="text/css" href="/assets/yui/container/assets/skins/sam/container.css">

	<%@ include file="/shared/template/includes/yui.jspf" %>

	<%-- protoype must load AFTER YUI. YUI doesn't extend elements, so the modalbox usages will fail --%>
	<script type="text/javascript" language="javascript" src="/assets/javascript/prototype.js"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/scriptaculous.js?load=effects,builder"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/modalbox.js"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/FD_GiftCards.js"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/common_javascript.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/callcenter_javascript.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_iframe.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_overtwo.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/iframecontentmws.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_scroll.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_shadow.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_draggable.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/timepicker/picker.js"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/timeslots.js"></script>

<% if ("true".equals(request.getAttribute("needsCCL"))) {%>
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


	<script type="text/javascript" language="javascript" src="/assets/javascript/phone_number.js"></script>

	<tmpl:get name='styles'/>
</head>


<body onload="<%=request.getAttribute("bodyOnLoad")%>" onunload="<%=request.getAttribute("bodyOnUnload")%>">
	<div class="crm_container">
		<div class="content">
		<%@ include file="/includes/context_help.jspf" %>

			<%-- header on top and content below --%>

			<jsp:include page="/includes/main_nav.jsp"/>
			
			<jsp:include page='/includes/customer_header.jsp'/>
			
			<jsp:include page='/includes/case_header.jsp'/>

			<tmpl:get name="content"/>
		</div>
		<div class="footer"><jsp:include page='/includes/copyright.jsp'/></div>
	</div>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000"></div>
</body>
</html>
