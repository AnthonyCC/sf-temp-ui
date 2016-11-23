<!DOCTYPE html>
<%@ page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.soy.SoyTemplateEngine"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopHelper"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri='logic' prefix='logic' %>
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String mobweb_uri = request.getRequestURI();
	boolean isQS = (mobweb_uri.indexOf("/quickshop/") != -1) ? true : false;
	boolean isCheckout = (mobweb_uri.indexOf("/expressco/") != -1) ? true : false;
	Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");
	boolean useFdxGlobalNav = FDStoreProperties.isFdxLocationbarEnabled();
	
	request.setAttribute("inMobWebTemplate", true);
%>
<%
	if (isQS) {
		%><features:isActive name="isQS20" featureName="quickshop2_0" /><%
	}

%>
<html>
  <head>
  
  	<tmpl:get name="seoMetaTag"/><%-- pages with just title text must use this tag --%>
  
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
  	<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
	<meta name="fragment" content="!">
    
    <%@ include file="/common/template/includes/i_javascripts_browse.jspf" %>
    
    <%-- THIS SETUP NEEDS TO BE BEFORE THE LOCABAR JS --%>
	<script>
		FreshDirect = FreshDirect || {};
		FreshDirect.locabar = FreshDirect.locabar || {};
		FreshDirect.locabar.isFdx = <%= useFdxGlobalNav %>;
		$jq.fn.messages = function( method ) {};
	</script>
	<%
		//any page that has timeslots needs prototype
		if (isCheckout || (mobweb_uri.indexOf("/your_account/reserve_timeslot.jsp") != -1) || (mobweb_uri.indexOf("/your_account/delivery_info_avail_slots.jsp") != -1)) {
			%><jwr:script src="/fdproto.js" useRandomParam="false" /><%
		}
	%>
	
	<fd:javascript src="/assets/javascript/locationbar.js" />
	<fd:javascript src="/assets/javascript/locationbar_fdx.js" />
    
    	
    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false" />
	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	
  	<tmpl:get name='facebookmeta'/>
  	
    <jwr:style src="/grid.css" media="all" />
    <jwr:style src="/oldglobal.css" media="all" />
    <jwr:style src="/global.css" media="all" />
	<fd:css href="/assets/css/common/locationbar_fdx.css" />
    <%
		if (isQS) {
			%><jwr:style src="/quickshop.css" media="all" /><%
		}
	%>
    <%
		if (isCheckout) {
			%><jwr:style src="/expressco.css" media="all" /><%
		}
	%>
    <jwr:style src="/mobileweb.css" media="all" /><%-- mobileweb should be last, for overriding --%>
    
    <tmpl:get name="extraCss" />
    <tmpl:get name="extraJs" />
    <tmpl:get name='nutritionCss'/>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
    
    <%
		if (isQS) {
			%><script type="text/javascript">
	        	function showStandardAds(){		
	        		$jq('#QSTop').show();
	        	}
        	</script><%
		}
	%>
  </head>
<!--[if lt IE 9]><body class="ie8" data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isQS) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><![endif]-->
<!--[if gt IE 8]><body data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isQS) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><![endif]-->
<!--[if !IE]><!--><body data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isQS) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><!--<![endif]-->
    <%@ include file="/shared/template/includes/i_body_start.jspf" %>
    <div class="container-fluid" id="page-content"><!-- body cont s -->
		
		<!-- top nav s -->
		<%@ include file="/common/template/includes/globalnav_mobileWeb_top.jspf"%>
		<!-- top nav e -->
		
		<div id="content" autoscroll="true"><!-- content starts here-->
			<!-- popcart s -->
				<%-- PLACEHOLDER(S) FOR NOW --%>
			<!-- popcart e -->
		
		   	<!-- messages s -->
		   	<jsp:include page="/shared/messages/messages_fdx.jsp" />
		  	
		  	<% if (FDStoreProperties.isAdServerEnabled()) {
				%><div id="OAS_SystemMessage">
  					<script type="text/javascript">OAS_AD('SystemMessage');</script>
  				</div>
			<% } else { %>
		    	<div class="message" data-type="system"><fd:GetSiteAnnouncements id="announcments" user="<%=user%>">
			    <logic:iterate id="ann" collection="<%=announcments%>" type="com.freshdirect.fdstore.FDSiteAnnouncementI">
			        <table width="100%" cellpadding="0" cellspacing="0" border="0">
			            <tr align="center">
			                <td>
			                    <font class="text12rbold"><%=ann.getHeader()%></font><br>
			                    <%=ann.getCopy()%>
			                    <br><img src="/media_stat/images/layout/clear.gif" width="1" height="6">
			                </td>
			            </tr>
			            <tr bgcolor="#999966"><td class="onePxTall"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
			        </table><br>
			    </logic:iterate></fd:GetSiteAnnouncements></div><%
			} %>

			<%@ include file="/common/template/includes/i_cutoff_warning.jspf"%>

    		<div class="message invisible" id="deliveryetawarning" data-type="deliveryetawarning"><%@ include file="/common/template/includes/i_delivery_eta_info.jspf"%></div>
    
		   	<!-- messages e -->
		   	
		   	<!-- modorder s -->
		   	<%-- THIS IS HIDDEN FROM DISPLAY FOR NOW --%>
		   	<div id="modifyorderalert_cont" class="">
			   	<div id="modifyorderalert" class="alerts invisible" data-type="modifyorderalert">
					<comp:modifyOrderBar user="<%= user %>" modifyOrderAlert="true" htmlId="test_modifyorderalert" />
				</div>
		   	</div>
		   	<!-- modorder e -->
		
			<section class="tabs" style="display: none;">
    			<!-- start : tabs -->
    			<tmpl:get name='tabs'/>
    			<!-- end : tabs -->      
  			</section>
		    <nav class="leftnav" style="display: none;">
		      <!-- start : leftnav -->
		      <tmpl:get name='leftnav'/>
		      <!-- end : leftnav -->    
		    </nav>
		    <%
				if (isQS) {
					%>
					<div id="quickshop"  class="container text10 <tmpl:get name='containerClass' />">
		                <div class="header">
		                  <h1 class='qs-title icon-reorder-icon-before notext'>Reorder</h1><span class="qs-subtitle"><strong>Smart shopping</strong> from <strong>past orders &amp; lists</strong></span>      
		                </div>
		                <% if(false){ %>
		                <div id="mm-reorder-nav">
							<ul>
								<li><a href="/quickshop/qs_top_items.jsp" class="cssbutton purple <% if(true){ %>non<% }%>transparent">Items</a></li>
								<li><a href="/quickshop/qs_past_orders.jsp" class="cssbutton purple <% if(true){ %>non<% }%>transparent">Orders</a></li>
								<li><a href="/quickshop/qs_shop_from_list.jsp" class="cssbutton purple <% if(true){ %>non<% }%>transparent">Lists</a></li>					
							</ul>
							<% if (user.isEligibleForStandingOrders()) { %>
								<div id="mm-reorder-nav-so"><a href="/quickshop/qs_standing_orders.jsp" class="cssbutton purple transparent">Standing Orders</a></div>                     
							<%} %>
				    	</div>
				    	<%} %>
				    		                
	                <tmpl:get name="pagination" />
	                
	                <h2>Items</h2>
	                <%
				}
			%>
			<tmpl:get name="content" />
		    <%
				if (isQS) { %>
					<tmpl:get name="pagination" />
					</div>
				<%}
			%>
	    </div><!-- content ends above here-->
	    
	    <!-- bottom nav s -->
	    <%@ include file="/common/template/includes/globalnav_mobileWeb_bottom.jspf" %>
	    <!-- bottom nav e -->
	</div><!-- body cont e -->
	<!-- body cont dialogs start -->
	<!-- body cont dialogs end -->

    <tmpl:get name="soytemplates" />
	<tmpl:get name='soypackage'/>
	
	<%
		if (isQS) { /* right place for this? */
			%><div id="ModifyBRDContainer"></div><%
		}
	%>
    
	
    <tmpl:get name="jsmodules" />
	<tmpl:get name='extraJsModules'/>
	    
	<jwr:script src="/mobileweb.js" useRandomParam="false" />
    <tmpl:get name="extraJsFooter" />
  </body>
</html>
