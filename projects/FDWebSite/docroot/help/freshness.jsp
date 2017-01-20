<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<fd:CheckLoginStatus />
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_nav_html5.jsp";
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com/help/freshness.jsp";
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}
	
%>
<tmpl:insert template='<%= pageTemplate %>'>
	<tmpl:put name='title' direct='true'>FreshDirect - Our Freshness Guarantee</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="freshness_guarantee" includeSiteSearchLink="true"/>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<%--
			Put any java-related variables needed by the page into the _page_options object. 
		--%>
		<%
			String language = "english";
	
			if ( request.getParameter("lang") != null) {
				if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) {
					language = "espanol";
				}
			}
		%>
		<script type="text/javascript">
			if (!window['_page_options']) {	var _page_options = {}; };
			_page_options['lang'] = '<%= language %>';
			_page_options['csEmail'] = '<fd:GetServiceEmail />';
			_page_options['csPhone'] = '<% if (user != null) { %><%=user.getCustomerServiceContact()%><%}%>';
		</script>
		
		<% if ( "espanol".equals(language) ) { %>
			<fd:IncludeMedia name="/media/editorial/site_pages/freshness_guarantee_espanol.html" />
		<% } else { %>
			<fd:IncludeMedia name="/media/editorial/site_pages/freshness_guarantee.html" />
		<% } %>
	</tmpl:put>
</tmpl:insert>