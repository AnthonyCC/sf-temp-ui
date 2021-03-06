<!DOCTYPE html>
<%@ page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserUtil" %>
<%@ page import="com.freshdirect.webapp.soy.SoyTemplateEngine"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ page import="com.freshdirect.webapp.ajax.quickshop.QuickShopHelper"%>
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
	boolean isReorder = (mobweb_uri.indexOf("/quickshop/") != -1) ? true : false;
	boolean isReorderItems = (mobweb_uri.indexOf("/qs_top_items.jsp") != -1) ? true : false;
	boolean isReorderLists = (mobweb_uri.indexOf("/qs_shop_from_list.jsp") != -1) ? true : false;
	boolean isReorderOrders = (mobweb_uri.indexOf("/qs_past_orders.jsp") != -1) ? true : false;
	boolean isCheckout = (mobweb_uri.indexOf("/expressco/") != -1) ? true : false;
	boolean isHelp = (mobweb_uri.indexOf("/help/") != -1) ? true : false;
	boolean isModifyOrder = FDUserUtil.getModifyingOrder(user) != null;

	Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");
	boolean useFdxGlobalNav = FDStoreProperties.isFdxLocationbarEnabled();

	request.setAttribute("inMobWebTemplate", true);
	
	boolean isChat = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.livechat, user);
%>
<%
	if (isReorder) {
		%><features:isActive name="isQS20" featureName="quickshop2_0" /><%
	}

%>
<html lang="en-US" xml:lang="en-US">
  <head>
<%--   	<title><tmpl:get name="title"/></title> --%>
  	<tmpl:get name="seoMetaTag"/><%-- if title is used, overrides previous tag --%>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
  	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="fragment" content="!">

    <fd:CanonicalPageLink/>
    <%@ include file="/common/template/includes/i_javascripts_browse.jspf" %>

    <%-- THIS SETUP NEEDS TO BE BEFORE THE LOCABAR JS --%>
	<script>
		FreshDirect = FreshDirect || {};
		FreshDirect.locabar = FreshDirect.locabar || {};
		FreshDirect.locabar.isFdx = <%= useFdxGlobalNav %>;
		$jq.fn.messages = function( method ) {};
	</script>
    <jwr:script src="/locabarcomp.js" useRandomParam="false" />

    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false" />
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>

  	<tmpl:get name='facebookmeta'/>
  	
  	

    <jwr:style src="/grid.css" media="all" />
    <jwr:style src="/oldglobal.css" media="all" />
    <jwr:style src="/global.css" media="all" />
	<jwr:style src="/locabarfdx.css" media="all" />
	<% if(request.getRequestURI().indexOf("/your_account/")>-1) { %>
			<jwr:style src="/assets/css/common/styles.css" media="all" />
			<jwr:style src="/assets/css/alerts_examples.css" media="all" />
			<jwr:script src="/assets/javascript/scripts.js" useRandomParam="false" />
			<jwr:script src="/assets/javascript/jquery.hint.js" useRandomParam="false" />
			<jwr:script src="/assets/javascript/jquery.pwstrength.js" useRandomParam="false" />
			<script>jQuery(function($jq) { $jq('#password1').pwstrength(); });</script>
	<% } %>

    <%
		if (isReorder) {
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
    <tmpl:get name="customCss" />
    <tmpl:get name="extraJs" />
    <tmpl:get name='nutritionCss'/>
	<% if (isModifyOrder) { %>
			<jwr:style src="/modifyorder.css" media="all" />
	<%} %>
    <%
		if ( (request.getRequestURI().indexOf("/your_account/giftcards.jsp")>-1) || (request.getRequestURI().indexOf("/your_account/gc_order_details.jsp")>-1) ) {
			//do nothing
		} else if(request.getRequestURI().indexOf("/your_account/")>-1) { %>
			<%@ include file="/shared/template/includes/ccl.jspf" %>
	<% } %>

    <%@ include file="/shared/template/includes/i_head_end.jspf" %>

    <%
		if (isReorder) {
			%><script type="text/javascript">
	        	function showStandardAds(){
	        		$jq('#QSTop').show();
	        	}
        	</script><%
		}
	%>
  </head>
<!--[if lt IE 9]><body class="ie8" data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-eventsource="<tmpl:get name='eventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><![endif]-->
<!--[if gt IE 8]><body data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-eventsource="<tmpl:get name='eventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><![endif]-->
<!--[if !IE]><!--><body data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-eventsource="<tmpl:get name='eventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><!--<![endif]-->
	
    <div class="container-fluid" id="page-content"><!-- body cont s -->

		<!-- top nav s -->
		<% if (request.getRequestURI().indexOf("/expressco/checkout.jsp") > -1) {
		%>
		<%@ include file="/common/template/includes/checkoutnav_mobileWeb.jspf"%>
		<% } else { %>
		<%@ include file="/common/template/includes/globalnav_mobileWeb_top.jspf"%>
		<%} %>
		<!-- top nav e -->
		
			<div id="content" autoscroll="true" class="<tmpl:get name='containerExtraClass'/>"><!-- content starts here-->
				<!-- popcart s -->
					<%-- PLACEHOLDER(S) FOR NOW --%>
				<!-- popcart e -->
	
			   	<!-- messages s -->
			   	<jsp:include page="/shared/messages/messages_fdx.jsp" />
			   	
				<% if (isChat) { %>
					<style>
						#bc-chat-container {
							left: auto !important;
							top: auto !important;
						}
					</style>
					<!-- BoldChat Live Chat Button HTML v5.00 (Type=HTML,ChatWindow=iOS v.01 9/10/2015 - Brooklyn,Department=- None -,Website=FreshDirect) -->
					<div id="open_live_chat">
					<script>
					  var bccbId = Math.random(); document.write(unescape('%3Cdiv id=' + bccbId + '%3E%3C/div%3E'));
					  window._bcvma = window._bcvma || [];
					  _bcvma.push(["setAccountID", "447701025416363034"]);
					  _bcvma.push(["setParameter", "WebsiteID", "2853440196463415121"]);
					  _bcvma.push(["addText", {type: "chat", window: "781368249134851385", available: "", unavailable: "", id: bccbId}]);
					  var bcLoad = function(){
					    if(window.bcLoaded) return; window.bcLoaded = true;
					    var vms = document.createElement("script"); vms.type = "text/javascript"; vms.async = true;
					    vms.src = ('https:'==document.location.protocol?'https://':'http://') + "vmss.boldchat.com/aid/447701025416363034/bc.vms4/vms.js";
					    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(vms, s);
					  };
					  if(window.pageViewer && pageViewer.load) pageViewer.load();
					  else if(document.readyState=="complete") bcLoad();
					  else if(window.addEventListener) window.addEventListener('load', bcLoad, false);
					  else window.attachEvent('onload', bcLoad);
					</script>
					</div>
					<!-- /BoldChat Live Chat Button HTML v5.00 -->
				<% } %>
				
			  	<% if (FDStoreProperties.isAdServerEnabled()) {
					%><div id="oas_SystemMessage">
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
				                    <br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="6">
				                </td>
				            </tr>
				            <tr bgcolor="#999966"><td class="onePxTall"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td></tr>
				        </table><br>
				    </logic:iterate></fd:GetSiteAnnouncements></div><%
				} %>
	
	    		<div class="message invisible" id="deliveryetawarning" data-type="deliveryetawarning"><%@ include file="/common/template/includes/i_delivery_eta_info.jspf"%></div>
	
			   	<!-- messages e -->
	
	
				<section class="tabs">
	    			<!-- start : tabs -->
	    			<tmpl:get name='tabs'/>
	    			<!-- end : tabs -->
	  			</section>
				<section class="main">
            <tmpl:get name='mobileSubMenu'/>
					
				    <nav class="leftnav" style="display: none;">
				      <!-- start : leftnav -->
				      <tmpl:get name='leftnav'/>
				      <!-- end : leftnav -->
				    </nav>
				    <%
						if (isReorder) {
							%>
							<div id="quickshop"  class="container text10 <tmpl:get name='containerClass' />">
				                <div class="header">
				                  <h1 class='qs-title icon-reorder-icon-before notext'>Reorder</h1><span class="qs-subtitle"><strong>Smart shopping</strong> from <strong>past orders &amp; lists</strong></span>
				                </div>
				                <% if(false){ //Remove if check when other pages done. This nav is hidden only because only Items page was optimized for mobile %>
				                <div id="mm-reorder-nav">
									<ul>
										<li><a href="/quickshop/qs_top_items.jsp" class="cssbutton purple <% if(isReorderItems){ %>non<% }%>transparent">Items</a></li>
										<li><a href="/quickshop/qs_past_orders.jsp" class="cssbutton purple <% if(isReorderOrders){ %>non<% }%>transparent">Orders</a></li>
										<li><a href="/quickshop/qs_shop_from_list.jsp" class="cssbutton purple <% if(isReorderLists){ %>non<% }%>transparent">Lists</a></li>
									</ul>
									<% if (user.isEligibleForStandingOrders()) { %>
										<div id="mm-reorder-nav-so"><a href="/quickshop/qs_standing_orders.jsp" class="cssbutton purple transparent">Standing Orders</a></div>
									<%} %>
						    	</div>
						    	<%} %>
		
		
			                <% if (isReorderItems) { %>
			                	<h2>Your Top Items</h2>
			                <% } %>
			               <% if (isReorderLists) { %>
			               		<h2>Your Shopping Lists</h2>
			               <% } %>
			               <% if (isReorderOrders) { %>
			               		<h2>Your Last Order</h2>
			               <% } %>
			                 <tmpl:get name="pagination" />
			                <%
						}
					%>
					<% if (isReorder) { %>
						<%-- container with qs-container is required --%>
						<div class="qs-container"><tmpl:get name="menu" /></div>
					<% } %>
					<section class="content">
						<tmpl:get name="content" />
					</section>
				    <% if (isReorder) { %>
						<tmpl:get name="pagination" />
						</div>
					<% } %>
		    
				</section>
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
		if (isReorder) { /* right place for this? */
			%><div id="ModifyBRDContainer"></div><%
		}
	%>

    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>

    <tmpl:get name="jsmodules" />
	<tmpl:get name='extraJsModules'/>
	
	<jwr:script src="/mobileweb.js" useRandomParam="false" />
    <tmpl:get name="extraJsFooter" />
    <tmpl:get name="customJsBottom" />

	<% if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){ %>
		<script>
			$jq(document).on('ready',  function() {
				FreshDirect.components.ifrPopup.open({ url: '/registration/tcaccept_lite.jsp', width: 320, height: 400});
			});
		</script>
	<% } %>
    <!-- leastPrioritizeJs, they are most likely not needed on page load and/or async-->
    <tmpl:get name="leastPrioritizeJs"/>
  </body>
</html>
