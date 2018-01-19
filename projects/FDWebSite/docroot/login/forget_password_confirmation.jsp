<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" /> 
<%
	//expanded page dimensions
	final int W_FORGET_PASSWORD_CONFIRMATION_TOTAL = 700;
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_space_border.jsp"; //default
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //default
		request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/index.jsp"); //change for OAS
	}
	String previousPage;
%>
<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - ID Confirmed - Security Word Confirmed"/>
    </tmpl:put>
	<%--     <tmpl:put name='title' direct='true'>FreshDirect - ID Confirmed - Security Word Confirmed</tmpl:put> --%>
    <tmpl:put name='content' direct='true'>
		<%@ include file="/login/includes/forget_password_confirmation.jspf" %>
	</tmpl:put>
</tmpl:insert>