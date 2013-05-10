<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>
    
	<jsp:include page="/shared/template/includes/server_info.jsp"/>

  	<fd:css href="/assets/css/common/jquery-ui_base/jquery-ui.css" /><%-- jQuery UI base, override in global.css --%>
	<link rel="stylesheet" type="text/css" href="/ccassets/javascript/timepicker/picker.css" >
	<link rel="stylesheet" type="text/css" href="/assets/css/giftcards.css" />
	<link rel="stylesheet" type="text/css" href="/assets/css/modalbox.css" />
	<fd:css href="/assets/css/common/freshdirect.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/css/crm.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/css/case.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" />
	<link rel="stylesheet" type="text/css" href="/ccassets/css/promo.css" />
	<%-- YUI --%>
	<!-- Sam Skin CSS -->
	<%-- when upgrading yui please create a new directory for the version --%>
	<link rel="stylesheet" type="text/css" href="/assets/yui-2.9.0/container/assets/skins/sam/container.css">
	<%-- horrible, horrible IE hacks. --%> 
		<!--[if lte IE 7]>
		   <%-- IE6 and 7 --%>
		   <style type="text/css">
				.cDetToolTip .arrow {
				    width: 0;
				    height: 0;
				    font-size: 0;
				    line-height: 0;
				    border-top: 16px solid #f2f2f2;
				    border-bottom: 0px solid transparent;
				    border-left: 8px solid transparent;
				    border-right: 8px solid transparent;
					margin-top: 0;
		        	overflow: hidden;
		        	bottom: -16px;
				}
			</style>
		<![endif]-->
		<!--[if IE 8]>
			<style type="text/css">
				.cDetToolTip .arrow {
					overflow: hidden:
					height: 16px;
					border-left: 2px solid #bbb;
					margin-top: -32px;
		        	bottom: -16px;
				}
				.cDetToolTip .arrow:after {
					border: none;
					top: -16px;
					margin-top: -16px;
					height: 0;
				    width: 0;
				    font-size: 0;
				    line-height: 0;
				    border-top: 32px solid transparent;
				    border-bottom: 32px solid transparent;
				    border-left: 32px solid #f2f2f2;
					background-color: transparent;
				}
			</style>
		<![endif]-->
	<%-- horrible, horrible IE hacks. --%>

	<%@ include file="/shared/template/includes/yui.jspf" %>

	<%-- protoype must load AFTER YUI. YUI doesn't extend elements, so the modalbox usages will fail --%>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<script type="text/javascript" language="javascript" src="/assets/javascript/FD_GiftCards.js"></script>

	
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/callcenter_javascript.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>
    <%-- These overlibs are required for the footer --%>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_iframe.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_overtwo.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/iframecontentmws.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_scroll.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_shadow.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_draggable.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/timepicker/picker.js"></script>
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
