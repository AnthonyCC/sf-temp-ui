<!DOCTYPE html>
<%@ page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserUtil" %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopHelper"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%
	/* ================================================================================
	 *	THIS IS A WIP VERSION FOR OPTIMIZATION TESTS
	 * 	DO NOT USE ON PAGES BESIDES INDEX WITHOUT TESTING
	 *	20170913 batchley
	 *
	 *	This template removes a large amount of unused (on index) code. This includes
	 *	shared support libraries. Things that will not work (there may be more):
	 *		Search Autocomplete, overlays that use ifrPopup, cart prod count (init'd)
	 * ================================================================================ */

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
	request.setAttribute("inMobWebTemplateOptimized", true); //used in menu includes
%>
<html lang="en-US" xml:lang="en-US">
  <head>
  <% if (isModifyOrder) { %>
  		<jwr:style src="/global.css" media="all" />
  <% } %>
<%--   	<title><tmpl:get name="title"/></title> --%>
  	
	<style>
		@charset "UTF-8"; 
		<%--
			All CSS should be inline, no bundle or external files 
			Include them directly, otherwise it affects performance
		--%>
		<%@ include file="/assets/css/mobileweb_index_optimized/mobileweb_index_optimized.css" %>
	</style>
    
	<%-- Keep the media include last, so it can always override any css auto-loaded --%>
	<fd:IncludeMedia name="/media/editorial/site_pages/stylesheet.html" />
	
  	<tmpl:get name="seoMetaTag"/><%-- if title is used, overrides previous tag --%>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
  	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="fragment" content="!">

  	<tmpl:get name='facebookmeta'/>

    <fd:CanonicalPageLink/>

    <tmpl:get name="extraCss" />
    <tmpl:get name='nutritionCss'/>
	<% if (isModifyOrder) { %>
			<jwr:style src="/modifyorder.css" media="all" />
	<%} %>
		<%-- Feature version switcher --%>
		<features:potato />
		<%-- LOAD JS --%>
		<jwr:script src="/mobileweb_index_optimized_jquerylibs.js" useRandomParam="false" />
		<script type="text/javascript">
			window.FreshDirect = window.FreshDirect || {};
			
			var FD = FreshDirect || {};
			FD.USQLegalWarning = FreshDirect.USQLegalWarning || {};
			FD.USQLegalWarning.sessionStore = '<%=session.getId()%>';
			FD.USQLegalWarning.getJSessionId = FD.USQLegalWarning.getJSessionId ||
			function () { return FD.USQLegalWarning.sessionStore; };
			
			var fd = window.FreshDirect;
			fd.libs = fd.libs || {};
			fd.libs.$ = jQuery;
	
		    <%-- THIS SETUP NEEDS TO BE BEFORE THE LOCABAR JS --%>
			FreshDirect.locabar = FreshDirect.locabar || {};
			FreshDirect.locabar.isFdx = <%= useFdxGlobalNav %>;
			$jq.fn.messages = function( method ) {};
			
			fd.features = fd.features || {};
			
			fd.features.active = <fd:ToJSON object="${featuresPotato.active}" noHeaders="true"/>
			
			fd.properties = fd.properties || {};
			fd.properties.isLightSignupEnabled = <%= FDStoreProperties.isLightSignupEnabled() ? "true" : "false" %>;
			fd.properties.isSocialLoginEnabled = <%= FDStoreProperties.isSocialLoginEnabled() ? "true" : "false" %>;
			fd.properties.isDebitSwitchNoticeEnabled = <%= FDStoreProperties.isDebitSwitchNoticeEnabled() ? "true" : "false" %>;
	
			
			<%-- copied from utils.js for dfp.js --%>
			fd.utils = fd.utils || {};
			fd.utils.getParameters = function (source) {
			  source = source || window.location.search.slice(1);
			
			  if (!source) {
			    return null;
			  }
			
			  var vars = {}, hash,
			      hashes = source.split('&');
			
			  hashes.forEach(function (h) {
			    hash = h.split('=');
			    vars[hash[0]] = window.decodeURIComponent(hash[1]);
			  });
			
			  return vars;
			};
			<%-- for debugging --%>
			fd.utils.readCookie = function(name) {
				var nameEQ = name + "=",
					ca = document.cookie.split(';'),
					i, c;
	
				for (i=0; i < ca.length; i++) {
					c = ca[i];
					while (c.charAt(0) === ' ') {
						c = c.substring(1, c.length);
					}
					if (c.indexOf(nameEQ) === 0) {
						return c.substring(nameEQ.length, c.length);
					}
				}
					return null;
			};
			fd.utils.isDeveloper = function () {
				return fd.utils.readCookie('developer');
			};
			
			if (fd.utils.isDeveloper()) {
				console.log('===== [ mobWeb optimized ] =====');
			}
			
			<%
				FDSessionUser jsUser = (FDSessionUser)session.getAttribute(SessionName.USER);
				if (jsUser != null) {
					int sessionUserLevel = jsUser.getLevel();
					FDSessionUser sessionUser = (FDSessionUser) jsUser;
					boolean hideZipCheckPopup = (!FDStoreProperties.isZipCheckOverLayEnabled() || (jsUser.getLevel() != FDSessionUser.GUEST || jsUser.isZipCheckPopupUsed() || sessionUser.isZipPopupSeenInSession()));
	
					if (!hideZipCheckPopup){
					    sessionUser.setZipPopupSeenInSession(true);
					}
			
					String zipCode = jsUser.getZipCode();
					String cohortName = jsUser.getCohortName();
					MasqueradeContext jsMasqueradeContext = jsUser.getMasqueradeContext();
		    	%>
					fd.user = {};
					fd.user.recognized = <%= sessionUserLevel == FDUserI.RECOGNIZED %>;
					fd.user.guest = <%= sessionUserLevel == FDUserI.GUEST %>;
					fd.mobWeb = <%= FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, jsUser) && JspMethods.isMobile(request.getHeader("User-Agent")) %>;
					fd.user.isZipPopupUsed = <%= hideZipCheckPopup %>;
					fd.user.zipCode = '<%= zipCode %>';
					fd.user.cohortName = '<%= cohortName %>';
					fd.user.sessionId = '<%=session.getId()%>';
					fd.user.isCorporateUser = <%= sessionUser.isCorporateUser() %>;
					<% if (jsMasqueradeContext != null) {%>
						fd.user.masquerade = true;
					<% } %>
				<% } %>
			(function () {
					
				<%-- updateOAS code --%>
					function OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query) {
						return OAS_url + 'adstream_mjx.ads/' +
								OAS_sitepage + '/1' + OAS_rns + '@' +
								OAS_listpos + '?' + OAS_query;
					}
	
					function done(listPos) {
						var $ = $jq;
						listPos.forEach(function (pos) {
							var selector = "#oas_"+pos+",#oas_b_"+pos;
							$(selector).each(function(i,e){
								if (FreshDirect.utils.isDeveloper()) {
									console.log('updateOAS: done', 'clearing elem html', pos, $(e));
								}
								$(e).html('');
								postscribe($(e), '<sc'+'ript>OAS_RICH("'+pos+'");</scr'+'ipt>', {
									error: function () {},
									done: function (pos) {
										$.each($('a[href*="/default/empty.gif/"]'), function(ii, ee) {
											$(ee).attr("tabindex", "-1");
											$(ee).attr("role", "presentation");
											$(ee).attr("aria-hidden", "true");
											if (fd.utils.isDeveloper()) {
												console.log('updateOAS: done', 'hiding "empty" oas pos', $(ee));
											}
										});
										
										if (fd.utils.isDeveloper()) {
											console.log('updateOAS: done', 'updated', $(e));
										}
									}
								});
							});
						});
					}
	
					function updateOAS(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query) {
						var scriptUrl = OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos.join(','), OAS_query);
	
						postscribe(document.body, '<sc'+'ript src="'+scriptUrl+'"></scr'+'ipt>', {
							done: function () {
							  done(OAS_listpos);
							}, error: function () {}
						});
					}
					FreshDirect.updateOAS = {};
					FreshDirect.updateOAS.done = done;
			}());
		</script>
		<jwr:script src="/mobileweb_index_optimized_everythingelse.js" useRandomParam="false" />
    	<jsp:include page="/common/template/includes/ad_server.jsp" flush="false" />

    	<tmpl:get name="extraJs" />
	</head>
<!--[if lt IE 9]><body class="ie8" data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-eventsource="<tmpl:get name='eventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><![endif]-->
<!--[if gt IE 8]><body data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-eventsource="<tmpl:get name='eventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><![endif]-->
<!--[if !IE]><!--><body data-ismobweb="true" <%= (isCheckout) ? "data-ec-page=" : "data-not-ec=" %>"<tmpl:get name="ecpage" />" data-printdata="<tmpl:get name='printdata'/>" data-eventsource="<tmpl:get name='eventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" <% if (isReorder) {%> data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"<% } %>><!--<![endif]-->
    
    <div class="container-fluid" id="page-content"><!-- body cont s -->

		<!-- top nav s -->
		<%@ include file="/common/template/includes/globalnav_mobileWeb_top.jspf"%>
		<!-- top nav e -->

		<div id="content" autoscroll="true"><!-- content starts here-->
			<!-- popcart s -->
				<%-- PLACEHOLDER(S) FOR NOW --%>
			<!-- popcart e -->

		   	<!-- messages s -->
		   	<%--<jsp:include page="/shared/messages/messages_fdx.jsp" />--%>
		   	<%-- inline so we can load js by itself --%>
			<div id="messages" class="visHidden">
				<ul class="content"></ul>
				<hr class="shadow">
				<a href="#" class="handler close-handler" onclick="return false;" id="locabar-messages-close"><span class="offscreen">close</span></a>
				<br class="NOMOBWEB" />
			</div>

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

			<%@ include file="/common/template/includes/i_cutoff_warning.jspf"%>

    		<div class="message invisible" id="deliveryetawarning" data-type="deliveryetawarning"><%@ include file="/common/template/includes/i_delivery_eta_info.jspf"%></div>

		   	<!-- messages e -->

			<section class="tabs">
    			<!-- start : tabs -->
    			<tmpl:get name='tabs'/>
    			<!-- end : tabs -->
  			</section>
		    <nav class="leftnav" style="display: none;">
		      <!-- start : leftnav -->
		      <tmpl:get name='leftnav'/>
		      <!-- end : leftnav -->
		    </nav>
		   
			<tmpl:get name="content" />
	    </div><!-- content ends above here-->

	    <!-- bottom nav s -->
	    <%@ include file="/common/template/includes/globalnav_mobileWeb_bottom.jspf" %>
	    <!-- bottom nav e -->
	</div><!-- body cont e -->
	<!-- body cont dialogs start -->
	<!-- body cont dialogs end -->


			
			<%@ include file="/common/template/includes/extol_tags.jspf" %>
			
			<%-- GTM initialization --%>
			<%@include file="/common/template/includes/i_gtm_datalayer.jsp" %>
			
			<fd:IncludeMedia name="/media/editorial/site_pages/javascript.html"/>
		
			
			<% 
			//System.out.println("DELIVERYADDRESS_COMPLETE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+session.getAttribute("DELIVERYADDRESS_COMPLETE")); 
			if (session.getAttribute("DELIVERYADDRESS_COMPLETE") != null) {%>	
					
			<script>
			$jq(document).ready(function() {
				if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { 
					FreshDirect.components.ifrPopup.open({ 
						url: '/social/delivery_address_create_success.jsp'}); 
				} else {
					pop('/social/delivery_address_create_success.jsp');
				}
			});
			</script>
			
		<% 
			session.setAttribute("DELIVERYADDRESS_COMPLETE",null);
			} 
		%>
		
		<% 
			if (session.getAttribute("SOCIAL_LOGIN_EXIST") != null) {%>	
					
			<script>
			$jq(document).ready(function() {
				if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { 
					FreshDirect.components.ifrPopup.open({ 
						url: '/social/social_account_exist.jsp'}); 
				} else {
					pop('/social/social_account_exist.jsp');
				}
			});
			</script>
			
		<% 
			session.setAttribute("SOCIAL_LOGIN_EXIST",null);
			} 
		%>

    <%-- tmpl:get name="jsmodules" / --%>
	<tmpl:get name='extraJsModules'/>

    <tmpl:get name="extraJsFooter" />

	<% if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){ %>
		<script>
			$jq(document).on('ready',  function() {
				FreshDirect.components.ifrPopup.open({ url: '/registration/tcaccept_lite.jsp', width: 320, height: 400});
			});
		</script>
	<% } %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
	<script>
		(function ($) {
			$(function () {
				$.smartbanner({daysHidden: 0, daysReminder: 0,author:'FreshDirect',button: 'VIEW'});
				if(!$jq('#smartbanner.shown').is(':visible')) { $jq('#smartbanner').show(); }
			});
		})($jq);
	</script>
   	<%-- //
   		we can't bundle dfp.js because it needs the global DFP_query (set in ad_server.jsp) before it's loaded
   		it also needs to be after all the ad positions, since it fires on parse and selects all spots
   		added to a footer bundle to minimize impact
   	// --%>
   	<% if (FDStoreProperties.isDfpEnabled()) { /* only load if needed */ %>
		<jwr:script src="/mobileweb_index_optimized_footer.js" useRandomParam="false" defer="true" async="true" />
	<% } %>
	<% if (isModifyOrder) { %>
		<%--
			Load soy.common and fd libs. When the users is in modify order mode, they must have landed in the view cart page already
			so this shouldn't cause any performance issue as these files have been cached by the browser.
		--%>

		<jwr:script src="/fdlibs.js" useRandomParam="false" />
		<soy:import packageName="common"/>
		<jwr:script src="/modifyorderdeps.js" useRandomParam="false" />
		<jwr:script src="/modifyorder.js" useRandomParam="false" async="true" defer="true" />
	<% } %>
  </body>
</html>
