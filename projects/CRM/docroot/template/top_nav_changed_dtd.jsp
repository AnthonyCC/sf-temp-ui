<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="/includes/i_globalcontext.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>
    
	<jsp:include page="/shared/template/includes/server_info.jsp"/>

	<%@ include file="/common/template/includes/i_stylesheets.jspf" %>

	<link rel="stylesheet" type="text/css" href="/assets/css/modalbox.css" />
	
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

	
	<%-- YUI --%>
	<!-- Sam Skin CSS -->
	<%-- when upgrading yui please create a new directory for the version --%>
	<link rel="stylesheet" type="text/css" href="/assets/yui-2.9.0/container/assets/skins/sam/container.css">
	

	<tmpl:get name='styles'/>
</head>



<body onload="<%=request.getAttribute("bodyOnLoad")%>" onunload="<%=request.getAttribute("bodyOnUnload")%>" CLASS="yui-skin-sam">
	<div class="crm_container">
	
		<div class="crm_globalnav">
			<jsp:include page="/includes/main_nav.jsp"/>
		</div>
		
		<%-- header on top and content below --%>
		<div class="content">
		<%@ include file="/includes/context_help.jspf" %>

			<%-- header on top and content below --%>			
			<jsp:include page='/includes/customer_header.jsp'/>
			
			<jsp:include page='/includes/case_header.jsp'/>

			<tmpl:get name="content"/>
		</div>
		<div class="crm_globalfooter"><jsp:include page='/includes/copyright.jsp'/></div>div>
	</div>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000"></div>
</body>
</html>
