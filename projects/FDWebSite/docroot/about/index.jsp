<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />

<% String siteAccessPage = request.getParameter("siteAccessPage"); 
   String jspTemplate = null;
   if(siteAccessPage!=null && siteAccessPage.equalsIgnoreCase("aboutus"))
	   jspTemplate = "/site_access/site_access.jsp";
   else
	   jspTemplate = "/common/template/no_space_border.jsp";

%>


<tmpl:insert template='<%= jspTemplate %>'>
<tmpl:put name='title' direct='true'>FreshDirect - About Us</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
if("dvlprs".equalsIgnoreCase(request.getParameter("catID"))){
%>
	<%@ include file="/includes/i_about_preamble.jspf"%>
<%
}
else{
	if(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("aboutus")){%>
		<fd:IncludeMedia name="/media/editorial/site_access/aboutus.ftl" withErrorReport="true"/>
	<%}else{ %>
		<fd:IncludeMedia name="/media/editorial/site_access/aboutus_siteaccess.ftl" withErrorReport="true"/>
	<% } %>
<% } %>
</tmpl:put>
</tmpl:insert>