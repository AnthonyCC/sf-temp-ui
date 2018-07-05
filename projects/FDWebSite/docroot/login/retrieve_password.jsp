<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.content.ContentFactory'%>
<%@ page import='com.freshdirect.fdstore.EnumEStoreId' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<fd:CheckLoginStatus id="user" />
<%
	String emailAddress = request.getParameter("email");
	String successPage = "/index.jsp";
	boolean isSuccess = false;
	if ( request.getParameter("success") != null && "true".equalsIgnoreCase(request.getParameter("success")) ) {
		isSuccess = true;
	}

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_site_nav.jsp"; //default
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //default
		String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com"+request.getRequestURI();
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
		}
		
		successPage = request.getRequestURI()+"?success=true";
	}
%>
<fd:ForgotPasswordController results="result" successPage='<%= successPage %>' password="password">	
<tmpl:insert template='<%= pageTemplate %>'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Reset Your Password"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Forgot Your Password? - Enter Security Word</tmpl:put> --%>
		<tmpl:put name='content' direct='true'>
			<%@ include file="/login/includes/retrieve_password.jspf" %>
		</tmpl:put>
	</tmpl:insert>
</fd:ForgotPasswordController>