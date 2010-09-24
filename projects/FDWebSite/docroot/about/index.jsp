<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<fd:CheckLoginStatus />

<%
	response.addHeader("Pragma", "no-cache"); 
	String noMobile = "FALSE";
	if ( request.getParameter("noMobile") != null ) {
		noMobile = request.getParameter("noMobile");
	}
	if (FDStoreProperties.isIphoneLandingEnabled()) {
		String UA = request.getHeader("User-Agent").toLowerCase();

		//check for iphone/ipod and change results
		if (UA.indexOf("iphone;")>=0 || UA.indexOf("ipod;")>=0) {
			//check that site access isn't returning an error from the POST...
			if ("FALSE".equals(noMobile) && "GET".equals(request.getMethod())){
				noMobile = "TRUE";
				%>
				<jsp:forward page="/mobile/index.jsp"/>
				<%
			}
		}
	}

	String siteAccessPage = request.getParameter("siteAccessPage"); 
	String jspTemplate = null;
	if ( siteAccessPage!=null && (siteAccessPage.equalsIgnoreCase("aboutus") || siteAccessPage.startsWith("c_")) ) {
		jspTemplate = "/site_access/site_access.jsp";
	}else{
		jspTemplate = "/common/template/no_space_border.jsp";
	}

	String baseUrl = "http://www.freshdirect.com";
	Map params = new HashMap();
	params.put("baseUrl", baseUrl);
%>


<tmpl:insert template='<%= jspTemplate %>'>
<tmpl:put name='title' direct='true'>FreshDirect - About Us</tmpl:put>
<tmpl:put name='content' direct='true'>
	<% if ("dvlprs".equalsIgnoreCase(request.getParameter("catID"))) { %>
		<%@ include file="/includes/i_about_preamble.jspf"%>
	<% }else{
	if(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("aboutus")){ %>
		<fd:IncludeMedia name="/media/editorial/site_access/aboutus.ftl" parameters="<%=params%>" withErrorReport="true"/>
	<% } %>
<% } %>
</tmpl:put>
</tmpl:insert>