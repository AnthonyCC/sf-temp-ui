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
  	
	<%@ include file="/common/template/includes/i_stylesheets.jspf" %>
	
	<%-- YUI --%>
	<!-- Sam Skin CSS -->
	<%-- when upgrading yui please create a new directory for the version --%>
	<link rel="stylesheet" type="text/css" href="/assets/yui-2.9.0/container/assets/skins/sam/container.css">
	
	

	<%@ include file="/shared/template/includes/yui.jspf" %>
	<%-- protoype must load AFTER YUI. YUI doesn't extend elements, so the modalbox usages will fail --%>
	
	    
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<script type="text/javascript" language="javascript" src="/assets/javascript/FD_GiftCards.js"></script>	
	<tmpl:get name='styles'/>
	
	<%@ include file="/includes/context_help.jspf" %>

	<tmpl:get name='styles'/>
	
	<!--  Added for Password Strength Display -->
	<!--  
    <script type="text/javascript" src="/assets/javascript/jquery-2.1.0.min.js"></script>
    -->
     <!--  Referred  jquery-1.7.2.min.js from FD-->
    <!-- <script type="text/javascript" src="/docroot/common/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="/assets/javascript/jquery.hint.js"></script>
	<script type="text/javascript" src="/assets/javascript/jquery.pwstrength.js"></script>-->
	
	<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
	
	<script type="text/javascript">
        jQuery(function($jq) { $jq('#password1').pwstrength(); });
  	 </script>
    <!--  Added for Password Strength Display -->
    
    <!--  Added for Password Strength Display -->
    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
	<!--  Added for Password Strength Display -->
</head>



<body onload="<%=request.getAttribute("bodyOnLoad")%>" onunload="<%=request.getAttribute("bodyOnUnload")%>" CLASS="yui-skin-sam">

	<div class="crm_globalnav">
		<jsp:include page="/includes/main_nav.jsp"/>
	</div>
	<div class="content">
		<%-- header on top and content below --%>		
		<jsp:include page='/includes/customer_header.jsp'/>		
		<jsp:include page='/includes/case_header.jsp'/>
		<tmpl:get name="content"/>
	</div>
	<div class="crm_globalfooter"><jsp:include page='/includes/copyright.jsp'/></div>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000"></div>
</body>
</html>
