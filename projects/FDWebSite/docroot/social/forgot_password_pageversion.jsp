<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='true' recognizedAllowed='true' id='user' />
<%
	//expanded page dimensions
	final int W_FORGET_PASSWORD_TOTAL = 700;
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_nav.jsp"; //default
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //default
		request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/index.jsp"); //change for OAS
	}
%>
<fd:ForgotPasswordController results="result" successPage='/login/forget_password_confirmation.jsp' password="password">	
	<tmpl:insert template='<%= pageTemplate %>'>
		<tmpl:put name="seoMetaTag" direct='true'>
			<fd:SEOMetaTag title="FreshDirect - Forgot Your Password? - Enter E-mail Address"/>
		</tmpl:put>
		<%--   <tmpl:put name='title' direct='true'>FreshDirect - Forgot Your Password? - Enter E-mail Address</tmpl:put> --%>
		<tmpl:put name='content' direct='true'>
			<%@ include file="/social/i_forgot_password_fields.jspf" %>
		</tmpl:put>
	</tmpl:insert>
</fd:ForgotPasswordController>