<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus/>

<% String siteAccessPage = request.getParameter("siteAccessPage"); 
   String jspTemplate = null;
   if(siteAccessPage!=null && siteAccessPage.equalsIgnoreCase("tour"))
	   jspTemplate = "/site_access/site_access.jsp";
   else
	   jspTemplate = "/common/template/left_dnav.jsp";
   %>
<tmpl:insert template='<%= jspTemplate %>'>
<tmpl:put name='title' direct='true'>FreshDirect - About FreshDirect</tmpl:put>
<tmpl:put name='content' direct='true'>
	<fd:IncludeMedia name="/media/editorial/site_access/tour/sort_ship_index.html" />
</tmpl:put>
</tmpl:insert>