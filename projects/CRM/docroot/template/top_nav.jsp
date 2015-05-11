<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="java.util.Calendar" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>

	<jsp:include page="/shared/template/includes/server_info.jsp"/>
        
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  	<fd:css href="/assets/css/common/jquery-ui_base/jquery-ui.css" /><%-- jQuery UI base, override in global.css --%>
	<link rel="stylesheet" type="text/css" href="/ccassets/javascript/timepicker/picker.css" >
	<link rel="stylesheet" type="text/css" href="/assets/css/giftcards.css" />
	<fd:css href="/assets/css/common/freshdirect.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/javascript/timepicker/picker.css" >
	<link rel="stylesheet" type="text/css" href="/ccassets/css/crm.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/css/case.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/css/promo.css" />
	<%-- YUI --%>
	<!-- Sam Skin CSS -->
	<%-- when upgrading yui please create a new directory for the version --%>
	<link rel="stylesheet" type="text/css" href="/assets/yui-2.9.0/container/assets/skins/sam/container.css">

	<%@ include file="/shared/template/includes/yui.jspf" %>

	<%-- protoype must load AFTER YUI. YUI doesn't extend elements, so the modalbox usages will fail --%>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<script type="text/javascript" language="javascript" src="/assets/javascript/FD_GiftCards.js"></script>

	
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

	<script language="javascript">
		/* temp fix for CRM errors */
		var $E  = YAHOO.util.Event; 	 
		var $D  = YAHOO.util.Dom; 	 
		//var $DH = YAHOO.ext.DomHelper; 	 
		var $C  = YAHOO.util.Connect; 	 
		var $X  = YAHOO.ext;
	</script>

	<tmpl:get name='styles'/>
	
	<!--  Added for Password Strength Display -->
    <script type="text/javascript" src="/assets/javascript/jquery-2.1.0.min.js"></script>
	<script type="text/javascript" src="/assets/javascript/jquery.hint.js"></script>
	<script type="text/javascript" src="/assets/javascript/jquery.pwstrength.js"></script>
	<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
	
	<script type="text/javascript">
        jQuery(function($) { $('#password1').pwstrength(); });
  	 </script>
    <!--  Added for Password Strength Display -->
    
    <!--  Added for Password Strength Display -->
    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
	<!--  Added for Password Strength Display -->
</head>



<body onload="<%=request.getAttribute("bodyOnLoad")%>" onunload="<%=request.getAttribute("bodyOnUnload")%>" CLASS="yui-skin-sam">
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
