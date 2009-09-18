<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus/>

<% String siteAccessPage = request.getParameter("siteAccessPage"); 
   String jspTemplate = null;
   if(siteAccessPage!=null && siteAccessPage.equalsIgnoreCase("tips"))
	   jspTemplate = "/site_access/site_access.jsp";
   else
	   jspTemplate = "/common/template/dnav.jsp";
   %>



<tmpl:insert template='<%= jspTemplate %>'>
<tmpl:put name='title' direct='true'>FreshDirect - About Us: Tips & Tricks</tmpl:put>
<tmpl:put name='content' direct='true'>
	<fd:IncludeMedia name="/media/editorial/site_access/tips.ftl" withErrorReport="true"/>	
</tmpl:put>
</tmpl:insert>