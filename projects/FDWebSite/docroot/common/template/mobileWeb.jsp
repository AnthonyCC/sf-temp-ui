<!DOCTYPE html>
<%@ page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
%>
<html>
  <head>
    <title><tmpl:get name="title" /></title>
  
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
  	<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
	<meta name="fragment" content="!">
    <%@ include file="/common/template/includes/metatags.jspf" %>
    
    <%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
    	
	<fd:LocationHandler/>
	<%
		AddressModel selectedAddress = (AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR);
		boolean hasFdxServices = true; /* just true in locationbar as well */  //LocationHandlerTag.hasFdxService( ((selectedAddress!=null) ? selectedAddress.getZipCode() : null) );
	%>
	<script>
		FreshDirect = FreshDirect || {};
		FreshDirect.locabar = FreshDirect.locabar || {};
		FreshDirect.locabar.zipcode = <%= ((selectedAddress!=null) ? selectedAddress.getZipCode() : null) %>;
		FreshDirect.locabar.hasFdxServices = <%=hasFdxServices %>;
		FreshDirect.locabar.selectedAddress = {
			type: null,
			address: '<%= LocationHandlerTag.formatAddressTextWithZip(selectedAddress) %>'
		};
	</script>
	
    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false" />
    <jwr:style src="/grid.css" media="all" />
    <jwr:style src="/oldglobal.css" media="all" />
    <jwr:style src="/global.css" media="all" />
    <jwr:style src="/mobileweb.css" media="all" />
    
    <tmpl:get name="extraCss" />
    <tmpl:get name="extraJs" />
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  </head>
<!--[if lte IE 9]><body class="ie"><![endif]-->
<!--[if gt IE 9]><!--><body><!--<![endif]-->

	
    <%@ include file="/shared/template/includes/i_body_start.jspf" %>
    <div class="container-fluid" id="page-content"><!-- body cont s -->
    
	   	<% if (FDStoreProperties.isAdServerEnabled()) {
			%><div id="OAS_SystemMessage">
	  			<script type="text/javascript">OAS_AD('SystemMessage');</script>
	  		</div><% 
	  	} %>
		
	    <tmpl:get name="nav_top" />
		
		<div id="content" autoscroll="true"><!-- content starts here-->
			<tmpl:get name="content" />
	    </div><!-- content ends above here-->
	    
	    <tmpl:get name="nav_bottom" />
	</div><!-- body cont e -->

    <tmpl:get name="soytemplates" />
    <tmpl:get name="jsmodules" />
    
	<jwr:script src="/mobileweb.js" useRandomParam="false" />
    <tmpl:get name="extraJsFooter" />
  </body>
</html>
