<!DOCTYPE html>
  
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ --%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<head>
  <title><tmpl:get name='title'/></title>
    
  <%@ include file="/common/template/includes/metatags.jspf" %>
  <%-- skip i_javascripts_optimized.jspf and load all code in here --%>
  <%-- @ include file="/common/template/includes/i_javascripts_optimized.jspf" --%>
  
  <%-- START i_javascripts_optimized.jspf --%>
	<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
	<%@ page import='com.freshdirect.fdstore.sempixel.FDSemPixelCache' %>
	<%@ page import='com.freshdirect.fdstore.sempixel.SemPixelModel' %>
	<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
	<%@ page import='com.freshdirect.fdstore.customer.*' %>
	<%@ taglib uri='freshdirect' prefix='fd' %>
	<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
	<%-- this file will be included in the <head> tag --%>
		<%
			/* Google Analytics Pixel */
			SemPixelModel semPixel_GA = FDSemPixelCache.getInstance().getSemPixel("GoogleAnalytics");
			semPixel_GA.setParam("GAKey", FDStoreProperties.getGoogleAnalyticsKey());
		%>
		<%
			if(request.getRequestURI().endsWith("referee_signup.jsp") || request.getRequestURI().indexOf("invite") != -1) {
		%>
			<script type="text/javascript">                     
				var _gaq = _gaq || [];
				_gaq.push(['_setAccount', 'UA-20535945-1']);
				_gaq.push(['_setDomainName', '.freshdirect.com']);
				_gaq.push(['_setCustomVar',1,'RAF','Responder',1]);					 
				_gaq.push(['_trackPageview', '/registration/referee_signup.jsp']); 
				_gaq.push(['_setReferrerOverride', '/registration/referee_signup.jsp?utm_medium=internal&utm_source=raf&utm_campaign=raf']);
				(function() { var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true; ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js'; var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s); })();
			</script>
		<% } else { %>
			<fd:SemPixelIncludeMedia pixelNames="GoogleAnalytics" />
		<% } %>
		
		
		<jwr:script src="/fdlibs_opt.js" useRandomParam="false" />
		<jwr:script src="/fdproto.js" useRandomParam="false" />
		<%-- jwr:script src="/fdlibsyui.js"  useRandomParam="false" / --%>
		
		<script type="text/javascript">
	
			var FreshDirect = FreshDirect || {};
			FreshDirect.USQLegalWarning = FreshDirect.USQLegalWarning || {};
			FreshDirect.USQLegalWarning.sessionStore = '<%=session.getId()%>';
	
			var $jq;
			var jqInit = false; 
			function initJQuery() {
				if (typeof(jQuery) == 'undefined') {
					if (!jqInit) {
						jqInit = true;
					}
					setTimeout("initJQuery()", 100);
				} else {
					$jq = jQuery.noConflict();
				}
			}
			initJQuery();
		</script>
		
		<jwr:script src="/fdmisc.js" useRandomParam="false" />
		
		<% if (request.getRequestURI().indexOf("brownie_points.jsp") == -1)  { %>
			<jwr:script src="/commonjavascript.js" useRandomParam="false" />
		<% } else { %>
			<%-- jwr:script src="/composite_common.js" useRandomParam="false" / --%>
		<% } %>
			
		
		<%
			/* ConvergeTrack Pixel */
			SemPixelModel semPixel_CT = FDSemPixelCache.getInstance().getSemPixel("ConvergeTrack");
		
			//always include this
			semPixel_CT.setParam("landing", "true");
			
			if(session != null && (session.getAttribute("LITESIGNUP_COMPLETE") != null || session.getAttribute("regSuccess") != null)) {
				/* user just completed sign up lite or checkout sign up, add param */
				semPixel_CT.setParam("signUp", "true");
	
				FDUserI sem_user_CT = (FDUserI)session.getAttribute(SessionName.USER);
				if (sem_user_CT != null) {
					semPixel_CT.setParam("custId", sem_user_CT.getIdentity().getErpCustomerPK().toString());
				}
				session.removeAttribute("regSuccess"); /* remove sign up marker once used, so every other page isn't logged */
				if (request.getRequestURI().equalsIgnoreCase("/index.jsp")) {
					//remove this session attribute
					session.removeAttribute("LITESIGNUP_COMPLETE");
				}
			}
	
			%><fd:SemPixelIncludeMedia pixelNames="ConvergeTrack" /><%
	
			//kill params
			semPixel_CT.setParam("signUp", "false");
			semPixel_CT.setParam("landing", "false");
		%>
		
		<jwr:script src="/fdccl.js"  useRandomParam="false" />
	
		<fd:IncludeMedia name="/media/editorial/site_pages/javascript.html"/>
		
		<%
			FDUserI dpTcCheckUser = (FDUserI)session.getAttribute(SessionName.USER);
			FDSessionUser dpTcCheckSessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
	
			if (dpTcCheckUser != null && request.getRequestURI().indexOf("brownie_points.jsp") == -1 &&
					(dpTcCheckUser.getLevel() == FDSessionUser.SIGNED_IN && Boolean.FALSE.equals(dpTcCheckSessionUser.hasSeenDpNewTc()) && dpTcCheckUser.isDpNewTcBlocking())
				) {
				%>
					<script type="text/javascript">
			    		$jq(document).ready(function() {
				    		doOverlayWindow('/overlays/delivery_pass_tc.jsp?showButtons=true&count=true');
			    		});
			    	</script>
		    	<%
			}
		%>
	  
  <%-- END i_javascripts_optimized.jspf --%>
  
  <%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
  <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  <tmpl:get name='extraHead'/>
  
</head>

<body>
      
  <%@ include file="/shared/template/includes/i_body_start.jspf" %>       
  <%@ include file="/common/template/includes/globalnav_optimized.jspf" %>
     
  <div id="main" class="container staticpage">
    <tmpl:get name='nav'/>
    <tmpl:get name='content'/>
  </div>
  
  <%@ include file="/common/template/includes/footer.jspf" %> 
  <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
    
</body>
</html>
