<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="includes/fk_presenter_vars.jspf" %>
<fd:CheckLoginStatus guestAllowed='true' recognizedAllowed='true' id='user' />
<%

	boolean mobWeb = false; //FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "includes/fklayout_tmpl.jsp"; //default
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //default
		String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.foodkick.com"+request.getRequestURI();
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.foodkick.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.foodkick.com/", "www.foodkick.com/mobileweb/")); //change for OAS
		}
	}
%>
<tmpl:insert template='<%= pageTemplate %>'>
<%-- 	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: Link Expired</tmpl:put> --%>
   <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="Same-Day Food Delivery NYC | FoodKick: Link Expired"/>
   </tmpl:put>
	<tmpl:put name='content'>
		<section id="section_forget_password_link_expired" class="forgot_password_section">
			<br/><br/>
			<h3>Sorry, the reset password link has expired.</h3>
			<span>Please request a new link in the Foodkick app.</span>
			
			<!--  <br/>
			<button class="download_button purple">OPEN FOODKICK APP</button>
			-->
		</section>
	</tmpl:put>
</tmpl:insert>