<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<fd:CheckLoginStatus />

<%
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
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - About Us"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - About Us</tmpl:put> --%>
<tmpl:put name='content' direct='true'>
	<% if ("dvlprs".equalsIgnoreCase(request.getParameter("catID"))) { %>
		<%@ include file="/includes/i_about_preamble.jspf"%>
	<% }else{
		/* forward over to new about us home */
		if (!"/site_access/site_access.jsp".equals(jspTemplate)) { %><jsp:forward page="/department.jsp?deptId=about"/><% }
		
		if(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("aboutus")){ %>
			<% if ( request.getParameter("lang") != null) { %>
				<% if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) { %> 
					<fd:IncludeMedia name="/media/editorial/site_access/aboutus_espanol.ftl" parameters="<%=params%>" withErrorReport="true"/>
				<% } else { %>
					<fd:IncludeMedia name="/media/editorial/site_access/aboutus.ftl" parameters="<%=params%>" withErrorReport="true"/>
				<% } %>
			<% } else { %>
				<fd:IncludeMedia name="/media/editorial/site_access/aboutus.ftl" parameters="<%=params%>" withErrorReport="true"/>
			<% } %>
		<% 
	} %>
<% } %>

</tmpl:put>
</tmpl:insert>