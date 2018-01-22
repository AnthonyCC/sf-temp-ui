<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="includes/fk_presenter_vars.jspf" %>
<%@ include file="includes/password_flow_vars.jspf" %>
<fd:CheckLoginStatus id="user" />
<%
	/*
		LEAVE FOODKICK AS-IS
	*/
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
<%-- 	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: Create New Password</tmpl:put> --%>
   <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="Same-Day Food Delivery NYC | FoodKick: Create New Password"/>
   </tmpl:put>
	<tmpl:put name='content'>
		<script type="text/javascript">
			cmCreatePageviewTag("FORGETPASSRETPASS: retrieve_password.jsp","FDX_FORGETPASSRETPASS",null,null,"-_--_-FORGETPASSRETPASS: retrieve_password.jsp-_--_--_--_-FORGETPASSRETPASS: retrieve_password.jsp");
		</script>
		<% String fName = user.getFirstName(); %>
		<section id="forgot_password_retrieve_section" class="forgot_password_section">
			<fd:ForgotPasswordController results="result" successPage='${FKAPP_DIR}/forget_password_changed.jsp' password="password">	
				<%@ include file="/login/includes/retrieve_password.jspf" %>	
			</fd:ForgotPasswordController>
		</section>
	</tmpl:put>
</tmpl:insert>