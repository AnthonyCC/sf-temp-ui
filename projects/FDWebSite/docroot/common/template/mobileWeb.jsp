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
  
  	<tmpl:get name="seoMetaTag"/><%-- pages with just title text must use this tag --%>
  
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
  	<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
	<meta name="fragment" content="!">
    
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
	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	
  	<tmpl:get name='facebookmeta'/>
  	
    <jwr:style src="/grid.css" media="all" />
    <jwr:style src="/oldglobal.css" media="all" />
    <jwr:style src="/global.css" media="all" />
    <jwr:style src="/mobileweb.css" media="all" />
    
    <tmpl:get name="extraCss" />
    <tmpl:get name="extraJs" />
    <tmpl:get name='nutritionCss'/>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  </head>
<!--[if lt IE 9]><body class="ie8" data-cmeventsource="<tmpl:get name='cmeventsource'/>"><![endif]-->
<!--[if gt IE 8]><body data-cmeventsource="<tmpl:get name='cmeventsource'/>"><![endif]-->
<!--[if !IE]><!--><body data-cmeventsource="<tmpl:get name='cmeventsource'/>"><!--<![endif]-->
	
    <%@ include file="/shared/template/includes/i_body_start.jspf" %>
    
    <div class="container-fluid" id="page-content"><!-- body cont s -->
    
	   	<% if (FDStoreProperties.isAdServerEnabled()) {
			%><div id="OAS_SystemMessage">
	  			<script type="text/javascript">OAS_AD('SystemMessage');</script>
	  		</div><% 
	  	} %>
		
		<!-- top nav s -->
		<%@ include file="/common/template/includes/globalnav_mobileWeb_top.jspf"%>
		<!-- top nav e -->
		
		<div id="content" autoscroll="true"><!-- content starts here-->
			<tmpl:get name="content" />
	    </div><!-- content ends above here-->
	    
	    <!-- bottom nav s -->
	    <%@ include file="/common/template/includes/globalnav_mobileWeb_bottom.jspf" %>
	    <!-- bottom nav e -->
	</div><!-- body cont e -->
	<!-- body cont dialogs start -->
	<!-- body cont dialogs end -->

    <tmpl:get name="soytemplates" />
	<tmpl:get name='soypackage'/>
	
    <tmpl:get name="jsmodules" />
	<tmpl:get name='extraJsModules'/>
	    
	<jwr:script src="/mobileweb.js" useRandomParam="false" />
    <tmpl:get name="extraJsFooter" />
  </body>
</html>
